package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Disabled
class PasswordEncoderTest {

	@Test
	void testEncodePassword() {
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		String rawPassword = "nam2020";
		String encodedPassword = pwdEncoder.encode(rawPassword);
		
		System.out.println(encodedPassword);
		
		boolean matches = pwdEncoder.matches(rawPassword, encodedPassword);
		assertThat(matches).isTrue();
	}

}
