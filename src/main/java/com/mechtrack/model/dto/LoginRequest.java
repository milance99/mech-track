package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Login credentials for workshop owner")
public record LoginRequest(
    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    @Schema(description = "Workshop owner name", example = "mike_johnson")
    String username,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Schema(description = "Workshop owner password", example = "MikeWorkshop2024!")
    String password
) {}
