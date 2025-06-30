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
	public String checkUnique(@RequestParam("id") Integer id,
			@RequestParam("name") String name,
			@RequestParam("alias") String alias) {
		
		return this.service.checkUnique(id, name, alias);
	}

}
