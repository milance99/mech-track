package com.mechtrack.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Security configuration properties for the MechTrack application.
 * All sensitive values must be provided via environment variables.
 */
@Component
@ConfigurationProperties(prefix = "mechtrack.security")
@Data
@Validated
public class AppSecurityProperties {

    /**
     * JWT secret key for token signing. Must be at least 32 characters.
     * Environment variable: MECHTRACK_SECURITY_JWT_SECRET
     * Property: mechtrack.security.jwt-secret
     */
    @NotBlank(message = "JWT secret is required")
    @Size(min = 32, message = "JWT secret must be at least 32 characters")
    private String jwtSecret;

    /**
     * Access token expiration time in milliseconds (default: 15 minutes)
     * Environment variable: MECHTRACK_SECURITY_ACCESS_TOKEN_EXPIRATION_MS
     * Property: mechtrack.security.access-token-expiration-ms
     */
    private long accessTokenExpirationMs = 900000; // 15 minutes

    /**
     * Refresh token expiration time in milliseconds (default: 7 days)
     * Environment variable: MECHTRACK_SECURITY_REFRESH_TOKEN_EXPIRATION_MS
     * Property: mechtrack.security.refresh-token-expiration-ms
     */
    private long refreshTokenExpirationMs = 604800000; // 7 days


    /**
     * Workshop owner's name/username
     * Environment variable: MECHTRACK_SECURITY_OWNER_NAME
     * Property: mechtrack.security.owner-name
     */
    @NotBlank(message = "Workshop owner name is required")
    private String ownerName;

    /**
     * Workshop owner's password (will be hashed)
     * Environment variable: MECHTRACK_SECURITY_OWNER_PASSWORD
     * Property: mechtrack.security.owner-password
     */
    @NotBlank(message = "Workshop owner password is required")
    @Size(min = 8, message = "Workshop owner password must be at least 8 characters")
    private String ownerPassword;

    /**
     * Allowed CORS origins (comma-separated)
     * Environment variable: MECHTRACK_SECURITY_CORS_ORIGINS
     * Property: mechtrack.security.cors-origins
     */
    private String corsOrigins = "http://localhost:3000";

    /**
     * Application environment (dev, prod, test)
     * Environment variable: MECHTRACK_SECURITY_ENVIRONMENT
     * Property: mechtrack.security.environment
     */
    private String environment = "dev";
}
