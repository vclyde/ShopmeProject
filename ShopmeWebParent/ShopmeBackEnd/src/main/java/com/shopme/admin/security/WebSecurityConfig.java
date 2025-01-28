package com.shopme.admin.security;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

	@Bean
	UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}

	@Bean
	SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {
		http.authenticationProvider(new DaoAuthenticationProvider() {
			{
				setUserDetailsService(userDetailsService());
				setPasswordEncoder(passwordEncoder());
			}
		});
		http.authorizeHttpRequests(auth -> {
			// Allow access to static resources
			auth.requestMatchers("/images/**", "/modules/**", "/js/**", "style.css").permitAll()
					.requestMatchers("/users/**").hasAuthority("Admin")
					.requestMatchers("/categories/**").hasAnyAuthority("Admin", "Editor")
					.anyRequest().authenticated(); // Require authentication for other paths
		}).formLogin(form -> {
			// Permit custom login page with custom username
			form.loginPage("/login").usernameParameter("email").permitAll();
		}).logout(logout -> {
			logout.permitAll();
		}).rememberMe(rememberMe -> {
			// Key is used when
			rememberMe.key("ABCDefghijklmnopqrstuvwXYZ_1234567890")
					.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(7));
		});

		return http.build();
	}

	@Bean // Using bcrypt cryptographic algorithm
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
