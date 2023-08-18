package com.serendipity.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication //(exclude = { SecurityAutoConfiguration.class })
public class SerendipityApiApplication {
	private static final int STRENGTH = 12;

	public static void main(String[] args) {
		SpringApplication.run(SerendipityApiApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncode() {
		return new BCryptPasswordEncoder(STRENGTH);
	}
}