package com.shopme.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Category;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService service;

	@GetMapping("/categories")
	public String listAll(Model model) {
		List<Category> categories = service.listAllCategories();
		model.addAttribute("listCategories", categories);

		return "categories/categories";
	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttrib) {
		service.updateUserEnabledStatus(id, enabled);

		String status = enabled ? "enabled" : "disabled";
		String message = "The category ID " + id + " has been " + status + "!";

		redirectAttrib.addFlashAttribute("message", message);

		return "redirect:/categories";
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttrib) {
		return "";
	}

}
