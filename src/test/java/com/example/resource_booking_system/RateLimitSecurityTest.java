package com.example.resource_booking_system;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestDataConfig.class)
class RateLimitSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void authenticationRequestsAreRateLimited() throws Exception {

        String requestBody = """
                {
                    "username": "admin",
                    "password": "wrong-password"
                }
                """;

        // First 5 authentication attempts are allowed
        for (int i = 0; i < 5; i++) {

            mockMvc.perform(
                    post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
            );
        }

        // 6th request within the same 60-second window is blocked
        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isTooManyRequests());
    }
}