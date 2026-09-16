package com.example.resource_booking_system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_SECONDS = 60;

    private final Map<String, AttemptInfo> attempts =
            new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (!isAuthenticationEndpoint(request, path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientKey = request.getRemoteAddr();

        AttemptInfo attemptInfo =
                attempts.computeIfAbsent(
                        clientKey,
                        key -> new AttemptInfo()
                );

        synchronized (attemptInfo) {

            long currentTime = Instant.now().getEpochSecond();

            if (currentTime - attemptInfo.windowStart
                    >= WINDOW_SECONDS) {

                attemptInfo.windowStart = currentTime;
                attemptInfo.attempts = 0;
            }

            if (attemptInfo.attempts >= MAX_ATTEMPTS) {

                response.setStatus(
                        HttpStatus.TOO_MANY_REQUESTS.value()
                );

                response.setContentType("application/json");

                response.getWriter().write(
                        "{\"error\":\"Too many authentication attempts. Please try again later.\"}"
                );

                return;
            }

            attemptInfo.attempts++;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticationEndpoint(
            HttpServletRequest request,
            String path) {

        return "POST".equalsIgnoreCase(request.getMethod())
                && (
                "/auth/login".equals(path)
                        || "/auth/register".equals(path)
        );
    }

    private static class AttemptInfo {

        private long windowStart =
                Instant.now().getEpochSecond();

        private int attempts = 0;
    }
}