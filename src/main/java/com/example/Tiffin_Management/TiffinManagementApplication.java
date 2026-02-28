package com.example.Tiffin_Management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(excludeName = {
		"org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
		"org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"
})
public class TiffinManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiffinManagementApplication.class, args);
	}

}
