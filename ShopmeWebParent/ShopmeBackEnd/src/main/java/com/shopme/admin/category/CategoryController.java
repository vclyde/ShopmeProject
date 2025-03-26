package com.shopme.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Category;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@GetMapping
	public String listFirstPage(Model model) {
		// List<Category> categories = service.listAllCategories();
		// model.addAttribute("listCategories", categories);

		// return "categories/categories";
		return listByPage(1, model, "id", "asc", null);
	}

	@GetMapping("/page/{pageNum}")
	public String listByPage(@PathVariable int pageNum, Model model,
			@RequestParam(defaultValue = "id") String sortField,
			@RequestParam(defaultValue = "asc") String sortDir,
			@RequestParam(required = false) String keyword) {

		Page<Category> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Category> listCategories = page.getContent();

		long startCount = (pageNum - 1) * CategoryService.CAT_PER_PAGE + 1;
		long endCount = startCount + CategoryService.CAT_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir); // Sort direction
		model.addAttribute("reverseSortDir", reverseSortDir); // Reverse sort direction
		model.addAttribute("keyword", keyword);

		return "categories/categories";
	}

	@GetMapping("/new")
	public String newCategory(Model model) {

		model.addAttribute("pageTitle", "Create New Category");
		model.addAttribute("category", new Category());

		return "categories/category_form";
	}

	@GetMapping("/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttrib) {
		service.updateUserEnabledStatus(id, enabled);

		String status = enabled ? "enabled" : "disabled";
		String message = "The category ID " + id + " has been " + status + "!";

		redirectAttrib.addFlashAttribute("message", message);

		return "redirect:/categories";
	}

	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttrib) {
		return "redirect:/categories";
	}

}
