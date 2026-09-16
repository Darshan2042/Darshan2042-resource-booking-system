package com.example.resource_booking_system.service;

import com.example.resource_booking_system.dto.auth.LoginRequest;
import com.example.resource_booking_system.dto.auth.LoginResponse;
import com.example.resource_booking_system.dto.auth.RegisterRequest;
import com.example.resource_booking_system.entity.User;
import com.example.resource_booking_system.enums.Role;
import com.example.resource_booking_system.exception.BadRequestException;
import com.example.resource_booking_system.exception.UserNotFoundException;
import com.example.resource_booking_system.repository.UserRepository;
import com.example.resource_booking_system.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserDetailsService userDetailsService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    // ==========================================
    // LOGIN
    // ==========================================

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(
                request.getUsername()
        ).orElseThrow(() ->
                new UserNotFoundException(
                        "User not found"
                )
        );

        // Load Spring Security user details through
        // the application's UserDetailsService.
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.getUsername()
                );

        String token =
                jwtService.generateToken(userDetails);

        return new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }


    // ==========================================
    // REGISTER
    // ==========================================

    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new BadRequestException(
                    "Username already exists"
            );
        }

        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new BadRequestException(
                    "Email already exists"
            );
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // Every registered account is a USER.
        user.setRole(Role.USER);

        userRepository.save(user);
    }
}