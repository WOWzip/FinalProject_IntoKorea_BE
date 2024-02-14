package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordConfig {

	// @Bean을 적으면 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
	// 비밀번호 암호화 (Security에서는 필수로 비밀번호 암호화 시켜야함)
	@Bean
	public BCryptPasswordEncoder endcodePwd() {
		return new BCryptPasswordEncoder();
	}
	
}
