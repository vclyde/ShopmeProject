package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Category;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@GetMapping
	public String listFirstPage(Model model) {
		List<Category> categories = service.listAllCategories();
		model.addAttribute("listCategories", categories);

		return "categories/categories";
		// return listByPage(1, model, "id", "asc", null);
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

		List<Category> listCategories = service.listCategoriesUsedInForm();

		model.addAttribute("pageTitle", "Create New Category");
		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);

		return "categories/category_form";
	}

	@PostMapping("/save")
	public String saveCategory(Category category, RedirectAttributes redirectAttrib,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			Category savedCategory = service.save(category);

			String uploadDir = "../category-images/" + savedCategory.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {

			if (category.getImage().isEmpty()) {
				category.setImage("");
			}
			service.save(category);
		}

		redirectAttrib.addFlashAttribute("message",
				"The category has been saved successfully!");

		String keyword = category.getName();
		return "redirect:/categories/page/1?sortField=id&sortDir=asc&keyword=" + keyword;

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

		try {
			service.delete(id);
			
			String uploadDir = "../category-images/" + id;
			FileUploadUtil.removeDir(uploadDir);
			
			redirectAttrib.addFlashAttribute("message", "The category with ID " + id + " has been deleted successfully!");
		} catch (CategoryNotFoundException ex) {
			redirectAttrib.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/categories";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrib) {

		try {
			List<Category> listCategories = service.listCategoriesUsedInForm();
			Category category = service.get(id);

			model.addAttribute("category", category);
			model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
			model.addAttribute("listCategories", listCategories);

			return "categories/category_form";
		} catch (CategoryNotFoundException ex) {

			redirectAttrib.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories";
		}
	}

}
