package com.shopme.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository catRepo;

	public List<Category> listAllCategories() {
		return (List<Category>) catRepo.findAll();
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		catRepo.updateEnabledStatus(id, enabled);
	}
	
	public void delete(Integer id) {
		
	}
}
