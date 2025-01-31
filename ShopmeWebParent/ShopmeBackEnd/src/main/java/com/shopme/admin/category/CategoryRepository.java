package com.shopme.admin.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Category;

@Repository
public interface CategoryRepository
		extends CrudRepository<Category, Integer>, PagingAndSortingRepository<Category, Integer> {

	@Query("SELECT c FROM Category c WHERE CONCAT(c.name, ' ', c.alias) LIKE %?1%")
	public Page<Category> findAll(String keyword, Pageable pageable);

	@Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
}
