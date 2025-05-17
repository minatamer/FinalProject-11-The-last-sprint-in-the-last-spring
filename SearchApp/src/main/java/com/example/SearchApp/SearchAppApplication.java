package com.example.SearchApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.SearchApp.Clients")
public class SearchAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchAppApplication.class, args);
	}

}
