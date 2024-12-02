package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listAll(Model model) {
		List<User> listUsers = service.listAllUsers();

		model.addAttribute("listUsers", listUsers);

		return "users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listAllRoles();

		User user = new User();
		user.setEnabled(true); // Enabled by default

		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");

		return "user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttrib,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {

			String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(filename);
			User savedUser = service.save(user);

			String uploadDir = "user-photos/" + savedUser.getId();

			FileUploadUtil.cleanDir(uploadDir); // Delete other uploaded images before saving new image
			FileUploadUtil.saveFile(uploadDir, filename, multipartFile);
		} else {
			
			if (user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
			service.save(user);
		}

		redirectAttrib.addFlashAttribute("message", "The user has been saved successfully!");
		return "redirect:/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrib) {

		try {
			List<Role> listRoles = service.listAllRoles();
			User user = service.get(id);

			model.addAttribute("user", user);
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", "Edit User (ID:" + id + ")");

			return "user_form";
		} catch (UserNotFoundException e) {
			redirectAttrib.addFlashAttribute("message", e.getMessage());

			return "redirect:/users";
		}
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttrib) {

		try {
			service.delete(id);
			redirectAttrib.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully!");
		} catch (UserNotFoundException e) {
			redirectAttrib.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/users";
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttrib) {
		service.updateUserEnabledStatus(id, enabled);

		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID " + id + " has been " + status;

		redirectAttrib.addFlashAttribute("message", message);

		return "redirect:/users";
	}
}
