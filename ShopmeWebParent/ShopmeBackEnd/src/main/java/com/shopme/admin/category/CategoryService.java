package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CategoryService {

	public static final int CAT_PER_PAGE = 5;

	@Autowired
	private CategoryRepository catRepo;

	public List<Category> listAllCategories() {
		// return (List<Category>) catRepo.findAll();
		// return (List<Category>) catRepo.findAll(Sort.by("id"));
		List<Category> rootCategories = catRepo.findRootCategories();
		return listHierarchicalCategories(rootCategories);
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
		List<Category> hierarchicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));

			Set<Category> children = rootCategory.getChildren();
			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				listSubHierarchicalSubCategories(hierarchicalCategories, subCategory, 1);
			}
		}

		return hierarchicalCategories;
	}

	private void listSubHierarchicalSubCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
		Set<Category> children = parent.getChildren();
		int newSublevel = subLevel + 1;

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSublevel; i++) {
				name += "--";
			}
			name += subCategory.getName();

			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			listSubHierarchicalSubCategories(hierarchicalCategories, subCategory, subLevel + 1);
		}
	}

	public Category save(Category category) {
		return catRepo.save(category);
	}

	public void delete(Integer id) throws CategoryNotFoundException {
		Long countById = catRepo.countById(id);

		if (countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find category with id " + id);
		}

		catRepo.deleteById(id);
	}

	public Page<Category> listByPage(int pageNum, String sortField, String sortDir,
			String keyword) {

		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, CAT_PER_PAGE, sort);

		if (keyword != null) {
			return catRepo.findAll(keyword, pageable);
		}

		return catRepo.findAll(pageable);
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = catRepo.findAll();

		for (Category category : categoriesInDB) {
			if (category.getParent() == null) { // Null is the parent/root
				categoriesUsedInForm.add(new Category(category.getId(), category.getName()));

				Set<Category> children = category.getChildren();
				for (Category subCategory : children) {
					categoriesUsedInForm.add(new Category(subCategory.getId(), "--" + subCategory.getName()));
					listCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
				}
			}
		}

		return categoriesUsedInForm;
	}

	private void listCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCat : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCat.getName();
			categoriesUsedInForm.add(new Category(subCat.getId(), name));

			listCategoriesUsedInForm(categoriesUsedInForm, subCat, newSubLevel);
		}
	}

	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		catRepo.updateEnabledStatus(id, enabled);
	}

	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return catRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CategoryNotFoundException("Could not find category with ID: " + id);
		}
	}

}
