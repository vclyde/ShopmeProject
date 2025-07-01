package com.shopme.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.user.UserService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserRestController {

	@Autowired
	private UserService userService;

	@PostMapping("/users/check-email")
	public String checkDuplicateEmail(@RequestParam(defaultValue = "0") Integer id, @RequestParam String email) {
		return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}
}
