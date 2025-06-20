package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Disabled;

@Disabled
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false) // Retain table updates
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository repo;

	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "Manage everything");
		Role savedRole = repo.save(roleAdmin);

		assertThat(savedRole.getId()).isGreaterThan(0);
		System.out.println(savedRole.getId() + "-> " + savedRole.getName() + " " + savedRole.getDescription());
	}

	@Test
	public void testCreateRestRoles() {
		Role roleSalesperson = new Role("Salesperson",
				"Manage product price, customers, shipping, orders and sales report");
		Role roleEditor = new Role("Editor", "Manage categories, brands, products, articles and menus");
		Role roleShipper = new Role("Shipper", "View products, view orders, and update order status");
		Role roleAssistant = new Role("Assistant", "Manage questions and reviews");

		repo.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
	}
}
