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
     * Environment variable: MECHTRACK_JWT_SECRET
     */
    @NotBlank(message = "JWT secret is required")
    @Size(min = 32, message = "JWT secret must be at least 32 characters")
    private String jwtSecret;

    /**
     * JWT token expiration time in milliseconds (default: 24 hours)
     * Environment variable: MECHTRACK_JWT_EXPIRATION
     */
    private long jwtExpirationMs = 86400000; // 24 hours

    /**
     * Workshop owner's name/username
     * Environment variable: MECHTRACK_OWNER_NAME
     */
    @NotBlank(message = "Workshop owner name is required")
    private String ownerName;

    /**
     * Workshop owner's password (will be hashed)
     * Environment variable: MECHTRACK_OWNER_PASSWORD
     */
    @NotBlank(message = "Workshop owner password is required")
    @Size(min = 8, message = "Workshop owner password must be at least 8 characters")
    private String ownerPassword;

    /**
     * Allowed CORS origins (comma-separated)
     * Environment variable: MECHTRACK_CORS_ORIGINS
     */
    private String corsOrigins = "http://localhost:3000";

    /**
     * Application environment (dev, prod, test)
     * Environment variable: MECHTRACK_ENVIRONMENT
     */
    private String environment = "dev";
}
