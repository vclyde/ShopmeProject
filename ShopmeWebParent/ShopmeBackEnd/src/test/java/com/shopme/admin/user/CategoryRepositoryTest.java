package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;
import java.util.List;
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

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repo;

	@Disabled
	@Test
	public void testCreateRootCategory() {
		String name = "Electronics";
		Category category = new Category(name);
		category.setAlias(name);
		category.setImage("default.png");
		Category savedCategory = repo.save(category);

		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

//	@Disabled
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(15);
		Category subCategory = new Category("iPhone 16 Pro Max", parent);
		subCategory.setAlias("iPhone 16 Pro Max");
		subCategory.setImage("default.png");

		Category savedCategory = repo.save(subCategory);

		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

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
}
