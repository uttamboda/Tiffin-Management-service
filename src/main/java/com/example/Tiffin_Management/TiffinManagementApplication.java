package com.example.Tiffin_Management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(excludeName = {
		"org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
		"org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"
})
@EnableScheduling
public class TiffinManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiffinManagementApplication.class, args);
	}

}
