package com.patrick;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication sprintApplication = new SpringApplication(CustomerServiceApplication.class);
		sprintApplication.setBannerMode(Banner.Mode.OFF);
		sprintApplication.run(args);
	}
}
