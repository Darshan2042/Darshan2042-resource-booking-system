package com.example.resource_booking_system;

import com.example.resource_booking_system.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class ResourceBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				ResourceBookingSystemApplication.class,
				args
		);
	}
}