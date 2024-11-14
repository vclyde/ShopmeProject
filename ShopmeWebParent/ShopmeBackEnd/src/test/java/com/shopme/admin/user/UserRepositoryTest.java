package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class UserRepositoryTest {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userCVelasquez = new User("cvelasquez@aquariussystems.ph", "pass2024", "Clyde", "Velasquez");
		userCVelasquez.addRole(roleAdmin);
		
		User savedUser = repo.save(userCVelasquez);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRole() {
		
		User userMae = new User("mpili@icomm.ph", "test", "Mae Judelyn", "Pili");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userMae.addRole(roleEditor);
		userMae.addRole(roleAssistant);
		
//		User userJay = new User("jdomugho@icomm.ph", "test", "Jay Mark", "Domugho");
		User savedUser = repo.save(userMae);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
//		assertThat(List.of(listUsers).size());
	}
	
	@Test
	public void testGetUserById() {
		User userCvelasquez = repo.findById(1).get();
		assertThat(userCvelasquez).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userCvelasquez = repo.findById(1).get();
		userCvelasquez.setEnabled(true);
		userCvelasquez.setEmail("cvelasquez@gmail.com");
		
		repo.save(userCvelasquez);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userMae = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userMae.getRoles().remove(roleEditor);
		userMae.addRole(roleSalesperson);
		
		repo.save(userMae);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
		
//		repo.findById(userId);
	}
}
