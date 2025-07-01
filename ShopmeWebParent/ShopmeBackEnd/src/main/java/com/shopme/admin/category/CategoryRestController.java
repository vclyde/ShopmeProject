package com.shopme.admin.category;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cvelasquez
 */
@RestController
@RequestMapping("/categories")
public class CategoryRestController {

	private final CategoryService service;

	public CategoryRestController(CategoryService service) {
		this.service = service;
	}

	@PostMapping("/check-unique")
	public String checkUnique(@RequestParam(defaultValue = "0") Integer id,
			@RequestParam String name,
			@RequestParam String alias) {
		
		return this.service.checkUnique(id, name, alias);
	}
}
