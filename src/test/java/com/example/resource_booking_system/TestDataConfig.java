package com.example.resource_booking_system;

import com.example.resource_booking_system.entity.User;
import com.example.resource_booking_system.enums.Role;
import com.example.resource_booking_system.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestDataConfig {

    @Bean
    CommandLineRunner testUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.findByUsername("admin").isEmpty()) {

                User admin = new User();

                admin.setUsername("admin");
                admin.setEmail("admin@test.com");
                admin.setPassword(
                        passwordEncoder.encode("admin123")
                );
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user").isEmpty()) {

                User user = new User();

                user.setUsername("user");
                user.setEmail("user@test.com");
                user.setPassword(
                        passwordEncoder.encode("user123")
                );
                user.setRole(Role.USER);

                userRepository.save(user);
            }
        };
    }
}