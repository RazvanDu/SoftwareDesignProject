package com.Project.CatalogService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CatalogServiceApplication {

	public static void main(String[] args) {

		System.setProperty("spring.config.name", "catalog-server");

		SpringApplication.run(CatalogServiceApplication.class, args);

	}
}