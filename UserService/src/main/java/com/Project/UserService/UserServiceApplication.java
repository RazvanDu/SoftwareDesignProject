package com.Project.UserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {

		System.setProperty("spring.config.name", "user-server");

		SpringApplication.run(UserServiceApplication.class, args);

	}
}