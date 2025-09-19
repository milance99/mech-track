package com.mechtrack.service;

import com.mechtrack.AbstractMechtrackTest;
import com.mechtrack.exception.AuthenticationException;
import com.mechtrack.exception.TokenValidationException;
import com.mechtrack.model.dto.JwtResponse;
import com.mechtrack.model.dto.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationServiceTest extends AbstractMechtrackTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Should authenticate valid credentials")
    void shouldAuthenticateValidCredentials() {
        LoginRequest loginRequest = new LoginRequest("test_owner", "password123");

        JwtResponse response = authenticationService.authenticateUser(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo("test_owner");
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.accessTokenExpiresAt()).isNotNull();
    }

    @Test
    @DisplayName("Should reject invalid username")
    void shouldRejectInvalidUsername() {
        LoginRequest loginRequest = new LoginRequest("wrong_user", "password123");

        assertThatThrownBy(() -> authenticationService.authenticateUser(loginRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    @DisplayName("Should reject invalid password")
    void shouldRejectInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("test_owner", "wrong_password");

        assertThatThrownBy(() -> authenticationService.authenticateUser(loginRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    @DisplayName("Should validate valid JWT token")
    void shouldValidateValidJwtToken() {
        LoginRequest loginRequest = new LoginRequest("test_owner", "password123");
        JwtResponse jwtResponse = authenticationService.authenticateUser(loginRequest);

        String authHeader = "Bearer " + jwtResponse.accessToken();
        var result = authenticationService.validateToken(authHeader);

        assertThat(result.isValid()).isTrue();
        assertThat(result.getUsername()).isEqualTo("test_owner");
        assertThat(result.getExpiresAt()).isNotNull();
    }

    @Test
    @DisplayName("Should reject invalid JWT token")
    void shouldRejectInvalidJwtToken() {
        String authHeader = "Bearer invalid_token";

        assertThatThrownBy(() -> authenticationService.validateToken(authHeader))
                .isInstanceOf(TokenValidationException.class)
                .hasMessage("Invalid or expired token");
    }

    @Test
    @DisplayName("Should reject malformed authorization header")
    void shouldRejectMalformedAuthorizationHeader() {
        String authHeader = "InvalidFormat token";

        assertThatThrownBy(() -> authenticationService.validateToken(authHeader))
                .isInstanceOf(TokenValidationException.class)
                .hasMessage("Invalid authorization header");
    }

    @Test
    @DisplayName("Should reject null authorization header")
    void shouldRejectNullAuthorizationHeader() {
        assertThatThrownBy(() -> authenticationService.validateToken(null))
                .isInstanceOf(TokenValidationException.class)
                .hasMessage("Invalid authorization header");
    }
}
