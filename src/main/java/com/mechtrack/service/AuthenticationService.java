package com.mechtrack.service;

import com.mechtrack.config.AppSecurityProperties;
import com.mechtrack.exception.AuthenticationException;
import com.mechtrack.exception.TokenValidationException;
import com.mechtrack.model.auth.LoginAttempt;
import com.mechtrack.model.auth.RefreshTokenData;
import com.mechtrack.model.auth.TokenValidationResult;
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
    private final ConcurrentHashMap<String, RefreshTokenData> refreshTokens = new ConcurrentHashMap<>();

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        String ip = "default";
        checkRateLimit(ip);
        
        try {
            validateCredentials(loginRequest);
            attempts.remove(ip);
            
            String accessToken = jwtUtils.generateAccessToken(loginRequest.username());
            String refreshToken = jwtUtils.generateRefreshToken(loginRequest.username());
            
            Date accessTokenExpiration = jwtUtils.getExpirationFromJwtToken(accessToken);
            Date refreshTokenExpiration = jwtUtils.getExpirationFromJwtToken(refreshToken);
            
            storeRefreshToken(refreshToken, loginRequest.username(), refreshTokenExpiration);
            cleanupExpiredRefreshTokens();
            
            log.info("Authentication successful: {}", loginRequest.username());
            return new JwtResponse(accessToken, refreshToken, loginRequest.username(), accessTokenExpiration, refreshTokenExpiration);
            
        } catch (AuthenticationException e) {
            recordFailedAttempt(ip);
            throw e;
        }
    }

    public JwtResponse refreshAccessToken(String refreshToken) {
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            throw new TokenValidationException("Invalid refresh token");
        }

        RefreshTokenData tokenData = refreshTokens.get(refreshToken);
        if (tokenData == null || tokenData.isExpired()) {
            refreshTokens.remove(refreshToken);
            throw new TokenValidationException("Refresh token not found or expired");
        }

        String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
        String newAccessToken = jwtUtils.generateAccessToken(username);
        String newRefreshToken = jwtUtils.generateRefreshToken(username);
        
        Date accessTokenExpiration = jwtUtils.getExpirationFromJwtToken(newAccessToken);
        Date refreshTokenExpiration = jwtUtils.getExpirationFromJwtToken(newRefreshToken);

        refreshTokens.remove(refreshToken);
        storeRefreshToken(newRefreshToken, username, refreshTokenExpiration);
        cleanupExpiredRefreshTokens();

        log.info("Token refresh successful: {}", username);
        return new JwtResponse(newAccessToken, newRefreshToken, username, accessTokenExpiration, refreshTokenExpiration);
    }

    public void revokeRefreshToken(String refreshToken) {
        refreshTokens.remove(refreshToken);
        log.info("Refresh token revoked");
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

    private void storeRefreshToken(String refreshToken, String username, Date expiresAt) {
        refreshTokens.put(refreshToken, new RefreshTokenData(username, expiresAt));
    }

    private void cleanupExpiredRefreshTokens() {
        refreshTokens.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

}
