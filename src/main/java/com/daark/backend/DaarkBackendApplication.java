package com.daark.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.daark.backend")
public class DaarkBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaarkBackendApplication.class, args);
	}

}
