package com.example.WallApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.WallApp.Clients")
public class WallAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WallAppApplication.class, args);
	}

}
