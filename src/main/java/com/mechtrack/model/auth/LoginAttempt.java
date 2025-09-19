package com.mechtrack.model.auth;

import java.time.LocalDateTime;

public class LoginAttempt {
    private int failedAttempts = 0;
    private LocalDateTime lockedUntil;

    public void recordFailure() {
        failedAttempts++;
    }

    public void lockUntil(LocalDateTime time) {
        this.lockedUntil = time;
    }

    public boolean isLocked() {
        return lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil);
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }
}
