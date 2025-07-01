package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author cvelasquez
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
	
	
	@MockBean
	private CategoryRepository repo;
	
	@InjectMocks
	private CategoryService service;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateName() {
		Integer id = null;
		String name = "Computers";
		String alias = "abc";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		
		String result = service.checkUnique(id, name, alias);
		Assertions.assertThat(result).isEqualTo("DuplicateName");
		Assertions.assertThat(result).isNotEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateAlias() {
		Integer id = null;
		String name = "NameAbc";
		String alias = "computers";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);
		
		String result = service.checkUnique(id, name, alias);
		
		Assertions.assertThat(result).isNotEqualTo("DuplicateName");
		Assertions.assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnOk() {
		Integer id = null;
		String name = "NameAbc";
		String alias = "computers";
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		
		String result = service.checkUnique(id, name, alias);
		
		Assertions.assertThat(result).isEqualToIgnoringCase("Ok");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateName() {
		Integer id = 1;
		String name = "Computers";
		String alias = "abc";
		
		Category category = new Category(2, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		
		String result = service.checkUnique(id, name, alias);
		Assertions.assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateAlias() {
		Integer id = 1;
		String name = "NameAbc";
		String alias = "computers";
		
		Category category = new Category(2, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);
		
		String result = service.checkUnique(id, name, alias);
		
		Assertions.assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOk() {
		Integer id = 1;
		String name = "NameAbc";
		String alias = "computers";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);
		
		String result = service.checkUnique(id, name, alias);
		
		Assertions.assertThat(result).isEqualToIgnoringCase("Ok");
	}
}
