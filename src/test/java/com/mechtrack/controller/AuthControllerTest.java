package com.mechtrack.controller;

import com.mechtrack.AbstractMechtrackMvcTest;
import com.mechtrack.model.dto.JwtResponse;
import com.mechtrack.model.dto.LoginRequest;
import com.mechtrack.service.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest extends AbstractMechtrackMvcTest {

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Should login with valid credentials")
    void shouldLoginWithValidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test_owner", "password123");
        JwtResponse jwtResponse = new JwtResponse("jwt_token_here", "test_owner", new Date());
        
        when(authenticationService.authenticateUser(any(LoginRequest.class)))
                .thenReturn(jwtResponse);

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt_token_here"))
                .andExpect(jsonPath("$.username").value("test_owner"))
                .andExpect(jsonPath("$.expiresAt").exists());
    }

    @Test
    @DisplayName("Should validate token successfully")
    void shouldValidateTokenSuccessfully() throws Exception {
        String authHeader = "Bearer valid_token";
        
        // Create a proper TokenValidationResult mock
        AuthenticationService.TokenValidationResult validResult =
            new AuthenticationService.TokenValidationResult(true, "test_owner", new Date());

        when(authenticationService.validateToken(anyString()))
                .thenReturn(validResult);

        mvc.perform(post("/api/auth/validate")
                        .header("Authorization", authHeader))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.username").value("test_owner"))
                .andExpect(jsonPath("$.expiresAt").exists());
    }

    @Test
    @DisplayName("Should reject login with invalid credentials")
    void shouldRejectLoginWithInvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("wrong_user", "wrong_password");
        
        when(authenticationService.authenticateUser(any(LoginRequest.class)))
                .thenThrow(new com.mechtrack.exception.AuthenticationException("Invalid credentials"));

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Authentication Failed"))
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    @DisplayName("Should reject invalid token validation")
    void shouldRejectInvalidTokenValidation() throws Exception {
        String authHeader = "Bearer invalid_token";
        
        when(authenticationService.validateToken(authHeader))
                .thenThrow(new com.mechtrack.exception.TokenValidationException("Invalid or expired token"));

        mvc.perform(post("/api/auth/validate")
                        .header("Authorization", authHeader))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Token Validation Failed"))
                .andExpect(jsonPath("$.message").value("Invalid or expired token"));
    }

    @Test
    @DisplayName("Should handle malformed JSON in login request")
    void shouldHandleMalformedJsonInLoginRequest() throws Exception {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"invalid\" json }"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle missing authorization header in token validation")
    void shouldHandleMissingAuthorizationHeaderInTokenValidation() throws Exception {
        mvc.perform(post("/api/auth/validate"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
