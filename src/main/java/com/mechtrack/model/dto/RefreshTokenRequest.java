package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to refresh an access token using a valid refresh token")
public record RefreshTokenRequest(
    @NotBlank(message = "Refresh token is required")
    @Schema(
        description = "Valid refresh token obtained from login",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String refreshToken
) {}
