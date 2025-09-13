package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * JWT authentication response DTO
 */
@Schema(description = "JWT authentication response")
public record JwtResponse(
    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token,

    @Schema(description = "Token type", example = "Bearer")
    String type,

    @Schema(description = "Workshop owner name", example = "mike_johnson")
    String username,

    @Schema(description = "Token expiration time")
    Date expiresAt
) {
    public JwtResponse(String token, String username, Date expiresAt) {
        this(token, "Bearer", username, expiresAt);
    }
}
