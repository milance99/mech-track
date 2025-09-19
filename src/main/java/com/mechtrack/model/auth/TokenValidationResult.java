package com.mechtrack.model.auth;

import java.util.Date;

public record TokenValidationResult(
    boolean valid,
    String username,
    Date expiresAt
) {
    
    public boolean isValid() {
        return valid;
    }
    
    public String getUsername() {
        return username;
    }
    
    public Date getExpiresAt() {
        return expiresAt;
    }
}
