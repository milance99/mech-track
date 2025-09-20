package com.mechtrack.model.auth;

import java.time.LocalDateTime;

public record TokenValidationResult(
    boolean valid,
    String username,
    LocalDateTime expiresAt
) {
    
    public boolean isValid() {
        return valid;
    }
    
    public String getUsername() {
        return username;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
