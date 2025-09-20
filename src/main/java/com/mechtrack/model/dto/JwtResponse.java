package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "JWT authentication response containing access and refresh tokens")
public record JwtResponse(
    @Schema(description = "JWT access token for API authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken,

    @Schema(description = "JWT refresh token for obtaining new access tokens", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String refreshToken,

    @Schema(description = "Token type", example = "Bearer")
    String type,

    @Schema(description = "Workshop owner name", example = "mike_johnson")
    String username,

    @Schema(description = "Access token expiration time", example = "2025-09-19T12:15:00")
    LocalDateTime accessTokenExpiresAt,

    @Schema(description = "Refresh token expiration time", example = "2025-09-26T12:00:00")
    LocalDateTime refreshTokenExpiresAt
) {
    public JwtResponse(String accessToken, String refreshToken, String username, LocalDateTime accessTokenExpiresAt, LocalDateTime refreshTokenExpiresAt) {
        this(accessToken, refreshToken, "Bearer", username, accessTokenExpiresAt, refreshTokenExpiresAt);
    }

}
