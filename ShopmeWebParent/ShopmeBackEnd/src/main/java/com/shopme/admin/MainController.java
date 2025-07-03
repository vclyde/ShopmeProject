package com.shopme.admin;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/login")
	public String viewLoginPage(Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated()) {
			return "redirect:/";
		}
		return "login";
	}
}
