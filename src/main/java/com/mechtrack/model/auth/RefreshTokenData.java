package com.mechtrack.model.auth;

import java.util.Date;

public record RefreshTokenData(
    String username,
    Date expiresAt
) {
    
    public boolean isExpired() {
        return expiresAt.before(new Date());
    }
}
