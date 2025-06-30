package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Category;
import java.util.List;
import java.util.NoSuchElementException;

@Disabled
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repo;
	
	@Disabled
	@Test
	public void testCreateRootCategory() {
		String name = "Computers";
		Category category = new Category(name);
		category.setAlias(name);
		category.setImage("");
		category.setEnabled(true);
		Category savedCategory = repo.save(category);

		assertThat(savedCategory.getId()).isGreaterThan(0);

		name = "Electronics";
		category = new Category(name);
		category.setAlias(name);
		category.setImage("");
		category.setEnabled(true);
		savedCategory = repo.save(category);

		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Disabled
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(12);
		String name = "iPhone 16";

		Category subCategory = new Category(name, parent);
		subCategory.setAlias(name);
		subCategory.setImage("");
		subCategory.setEnabled(true);

		Category savedCategory = repo.save(subCategory);

		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Disabled
	@Test
	public void testGetCategory() {
		Category category = repo.findById(2).get();
		System.out.println(category.getName());

		Set<Category> children = category.getChildren();
		for (Category subCategory : children) {
			System.out.println(subCategory.getName());
		}

		assertThat(children.size()).isGreaterThan(0);
	}

	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categories = repo.findAll();

		for (Category category : categories) {

			// Null is the root level
			if (category.getParent() == null) {
				System.out.println(category.getName());

				Set<Category> children = category.getChildren();

				for (Category subCategory : children) {
					System.out.println("--" + subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}

	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCat : children) {
			for (int i = 0; i < newSubLevel; i++) {
				System.out.print("--");
			}
			System.out.println(subCat.getName());

			printChildren(subCat, newSubLevel);
		}
	}
	
	@Test
	public void testListRootCategories() {
		
		List<Category> listRoot = repo.findRootCategories();
		assertThat(listRoot.size()).isGreaterThan(0);
		System.out.println(listRoot.size());
		listRoot.forEach(category -> {
			System.out.println(category.getName());
		});
	}
	
	@Test
	public void testGetCategory2() {
		Integer id = 100;
		try {
			 Category cat = repo.findById(id).get();
			 System.out.println(cat);
		} catch (NoSuchElementException e) {
			System.out.println("Could not find category with ID: " + id);
		}
	}
	
	@Test
	public void testFindByName() {
		String name = "Computers";
		Category category = repo.findByName(name);
		
		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}
	
	@Test
	public void testFindByAlias() {
		String alias = "computers";
		Category category = repo.findByAlias(alias);
		
		assertThat(category).isNotNull();
		assertThat(category.getAlias()).isEqualTo(alias);
	}
}
