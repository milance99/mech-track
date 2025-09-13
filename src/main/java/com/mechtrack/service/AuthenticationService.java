package com.mechtrack.service;

import com.mechtrack.config.AppSecurityProperties;
import com.mechtrack.exception.AuthenticationException;
import com.mechtrack.exception.TokenValidationException;
import com.mechtrack.model.dto.JwtResponse;
import com.mechtrack.model.dto.LoginRequest;
import com.mechtrack.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final JwtUtils jwtUtils;
    private final AppSecurityProperties securityProperties;
    private final PasswordEncoder passwordEncoder;
    private final ConcurrentHashMap<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        String ip = "default";
        checkRateLimit(ip);
        
        try {
            validateCredentials(loginRequest);
            attempts.remove(ip);
            
            String jwt = jwtUtils.generateJwtToken(loginRequest.username());
            Date expirationDate = jwtUtils.getExpirationFromJwtToken(jwt);
            
            log.info("Authentication successful: {}", loginRequest.username());
            return new JwtResponse(jwt, loginRequest.username(), expirationDate);
            
        } catch (AuthenticationException e) {
            recordFailedAttempt(ip);
            throw e;
        }
    }

    public TokenValidationResult validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new TokenValidationException("Invalid authorization header");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateJwtToken(token)) {
            throw new TokenValidationException("Invalid or expired token");
        }

        String username = jwtUtils.getUsernameFromJwtToken(token);
        Date expiresAt = jwtUtils.getExpirationFromJwtToken(token);
        return new TokenValidationResult(true, username, expiresAt);
    }

    private void checkRateLimit(String ip) {
        LoginAttempt attempt = attempts.get(ip);
        if (attempt != null && attempt.isLocked()) {
            throw new AuthenticationException("Too many failed attempts. Try again later.");
        }
    }

    private void recordFailedAttempt(String ip) {
        attempts.compute(ip, (k, v) -> {
            if (v == null) v = new LoginAttempt();
            v.recordFailure();
            if (v.getFailedAttempts() >= 5) {
                v.lockUntil(LocalDateTime.now().plusMinutes(15));
            }
            return v;
        });
    }

    private void validateCredentials(LoginRequest loginRequest) {
        if (!securityProperties.getOwnerName().equals(loginRequest.username()) ||
            !passwordEncoder.matches(loginRequest.password(), securityProperties.getOwnerPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }
    }

    public static class TokenValidationResult {
        private final boolean valid;
        private final String username;
        private final Date expiresAt;

        public TokenValidationResult(boolean valid, String username, Date expiresAt) {
            this.valid = valid;
            this.username = username;
            this.expiresAt = expiresAt;
        }

        public boolean isValid() { return valid; }
        public String getUsername() { return username; }
        public Date getExpiresAt() { return expiresAt; }
    }

    private static class LoginAttempt {
        private int failedAttempts = 0;
        private LocalDateTime lockedUntil;

        void recordFailure() { failedAttempts++; }
        void lockUntil(LocalDateTime time) { lockedUntil = time; }
        boolean isLocked() { return lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil); }
        int getFailedAttempts() { return failedAttempts; }
    }
}
