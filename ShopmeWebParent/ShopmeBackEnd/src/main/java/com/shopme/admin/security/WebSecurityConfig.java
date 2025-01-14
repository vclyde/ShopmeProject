package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

	@Bean
	SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/images/**", "/modules/**", "/js/**", "style.css").permitAll() // Allow access to
																									// resources
					.anyRequest().authenticated(); // Require authentication for other paths
		}).formLogin(form -> {
			form.loginPage("/login") // login page
					.usernameParameter("email") // custom username
					.permitAll();
		});
		
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
