package com.mechtrack.model.auth;

import java.time.LocalDateTime;

public record RefreshTokenData(
    String username,
    LocalDateTime expiresAt
) {
    
    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }
}
