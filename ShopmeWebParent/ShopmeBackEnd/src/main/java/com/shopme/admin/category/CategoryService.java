package com.shopme.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {

	public static final int CAT_PER_PAGE = 5;

	@Autowired
	private CategoryRepository catRepo;

	public List<Category> listAllCategories() {
//		return (List<Category>) catRepo.findAll();
		return (List<Category>) catRepo.findAll(Sort.by("id"));
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

	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		catRepo.updateEnabledStatus(id, enabled);
	}

	public void delete(Integer id) {

	}
}
