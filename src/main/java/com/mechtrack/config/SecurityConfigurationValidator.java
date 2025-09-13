package com.mechtrack.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Validates security configuration on application startup
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityConfigurationValidator {

    private final AppSecurityProperties securityProperties;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void validateSecurityConfiguration() {
        log.info("🔐 Validating security configuration...");

        try {
            // Validate JWT secret
            if (securityProperties.getJwtSecret().length() < 32) {
                throw new IllegalStateException("JWT secret must be at least 32 characters long");
            }

            // Validate workshop owner credentials
            if (securityProperties.getOwnerName().length() < 3) {
                throw new IllegalStateException("Workshop owner name must be at least 3 characters long");
            }

            if (securityProperties.getOwnerPassword().length() < 8) {
                throw new IllegalStateException("Workshop owner password must be at least 8 characters long");
            }

            // Hash the password if it's not already hashed
            if (!securityProperties.getOwnerPassword().startsWith("$2")) {
                String hashedPassword = passwordEncoder.encode(securityProperties.getOwnerPassword());
                securityProperties.setOwnerPassword(hashedPassword);
                log.info("✅ Workshop owner password hashed successfully");
            }

            log.info("✅ Security configuration validated successfully");
            log.info("🔑 JWT expiration: {} hours", securityProperties.getJwtExpirationMs() / (1000 * 60 * 60));
            log.info("👤 Workshop owner: {}", securityProperties.getOwnerName());
            log.info("🌐 CORS origins: {}", securityProperties.getCorsOrigins());
            log.info("🏢 Environment: {}", securityProperties.getEnvironment());

        } catch (Exception e) {
            log.error("❌ Security configuration validation failed: {}", e.getMessage());
            log.error("💡 Please check the SECURITY_SETUP.md file for proper configuration");
            throw new IllegalStateException("Security configuration validation failed", e);
        }
    }
}
