package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
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

//	@Bean
//	AuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailsService());
//		authProvider.setPasswordEncoder(passwordEncoder());
//
//		return authProvider;
//	}

	@Bean
	SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {
		http.authenticationProvider(new DaoAuthenticationProvider() {{
			setUserDetailsService(userDetailsService());
			setPasswordEncoder(passwordEncoder());
		}});
		http.authorizeHttpRequests(auth -> {
			// Allow access to static resources
			auth.requestMatchers("/images/**", "/modules/**", "/js/**", "style.css").permitAll()
					// Require authentication for other paths
					.anyRequest().authenticated();
		}).formLogin(form -> {
			// Permit custom login page with custom username
			form.loginPage("/login").usernameParameter("email").permitAll();
		});

		return http.build();
	}

	@Bean // Using bcrypt cryptographic algorithm 
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
