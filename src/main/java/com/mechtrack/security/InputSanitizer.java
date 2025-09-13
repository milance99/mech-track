package com.mechtrack.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Input sanitization utility to prevent injection attacks and clean user input
 */
@Component
@Slf4j
public class InputSanitizer {

    // Patterns for dangerous characters and sequences
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "(?i)(union|select|insert|update|delete|drop|create|alter|exec|execute|script|javascript|vbscript|onload|onerror)", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern XSS_PATTERN = Pattern.compile(
        "(?i)<[^>]*>|javascript:|vbscript:|onload|onerror|alert\\(|document\\.|window\\.", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern PATH_TRAVERSAL_PATTERN = Pattern.compile(
        "(\\.\\./|\\.\\.\\\\|%2e%2e%2f|%2e%2e%5c)", 
        Pattern.CASE_INSENSITIVE
    );

    // Allowed characters for different input types
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s\\-_\\.@]+$");
    private static final Pattern FILENAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s\\-_\\.\\(\\)]+$");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s\\-_\\.,!?\\(\\)]+$");

    /**
     * Sanitize general text input (customer names, car models, etc.)
     */
    public String sanitizeText(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }

        String sanitized = input.trim();
        
        // Remove potential XSS
        sanitized = sanitized.replaceAll("(?i)<[^>]*>", "");
        sanitized = sanitized.replaceAll("(?i)javascript:", "");
        sanitized = sanitized.replaceAll("(?i)vbscript:", "");
        
        // Remove potential SQL injection patterns
        sanitized = SQL_INJECTION_PATTERN.matcher(sanitized).replaceAll("");
        
        // Limit length
        if (sanitized.length() > 255) {
            sanitized = sanitized.substring(0, 255);
            log.warn("Input truncated to 255 characters: {}", input.substring(0, Math.min(50, input.length())));
        }

        return sanitized;
    }

    /**
     * Sanitize description text (allows more characters)
     */
    public String sanitizeDescription(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }

        String sanitized = input.trim();
        
        // Remove XSS but allow more punctuation
        sanitized = sanitized.replaceAll("(?i)<[^>]*>", "");
        sanitized = sanitized.replaceAll("(?i)javascript:", "");
        sanitized = sanitized.replaceAll("(?i)vbscript:", "");
        
        // Remove SQL injection patterns
        sanitized = SQL_INJECTION_PATTERN.matcher(sanitized).replaceAll("");
        
        // Limit length for descriptions
        if (sanitized.length() > 1000) {
            sanitized = sanitized.substring(0, 1000);
            log.warn("Description truncated to 1000 characters");
        }

        return sanitized;
    }

    /**
     * Sanitize filename input
     */
    public String sanitizeFilename(String filename) {
        if (!StringUtils.hasText(filename)) {
            return filename;
        }

        String sanitized = filename.trim();
        
        // Remove path traversal attempts
        sanitized = PATH_TRAVERSAL_PATTERN.matcher(sanitized).replaceAll("");
        
        // Remove dangerous characters
        sanitized = sanitized.replaceAll("[<>:\"|?*\\\\]", "");
        
        // Limit length
        if (sanitized.length() > 100) {
            String extension = "";
            int lastDot = sanitized.lastIndexOf('.');
            if (lastDot > 0) {
                extension = sanitized.substring(lastDot);
                sanitized = sanitized.substring(0, Math.min(95, lastDot)) + extension;
            } else {
                sanitized = sanitized.substring(0, 100);
            }
        }

        return sanitized;
    }

    /**
     * Validate input against potential security threats
     */
    public boolean isSecureInput(String input) {
        if (!StringUtils.hasText(input)) {
            return true;
        }

        // Check for SQL injection patterns
        if (SQL_INJECTION_PATTERN.matcher(input).find()) {
            log.warn("Potential SQL injection attempt detected: {}", input.substring(0, Math.min(50, input.length())));
            return false;
        }

        // Check for XSS patterns
        if (XSS_PATTERN.matcher(input).find()) {
            log.warn("Potential XSS attempt detected: {}", input.substring(0, Math.min(50, input.length())));
            return false;
        }

        // Check for path traversal
        if (PATH_TRAVERSAL_PATTERN.matcher(input).find()) {
            log.warn("Potential path traversal attempt detected: {}", input.substring(0, Math.min(50, input.length())));
            return false;
        }

        return true;
    }

    /**
     * Validate filename security
     */
    public boolean isSecureFilename(String filename) {
        if (!StringUtils.hasText(filename)) {
            return false;
        }

        // Check basic security
        if (!isSecureInput(filename)) {
            return false;
        }

        // Check filename-specific patterns
        return FILENAME_PATTERN.matcher(filename).matches();
    }

    /**
     * Validate alphanumeric input (usernames, etc.)
     */
    public boolean isAlphanumeric(String input) {
        if (!StringUtils.hasText(input)) {
            return false;
        }
        
        return ALPHANUMERIC_PATTERN.matcher(input).matches();
    }
}
