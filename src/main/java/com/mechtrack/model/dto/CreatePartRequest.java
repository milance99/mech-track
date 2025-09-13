package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating or updating a part")
public class CreatePartRequest {
    
    @NotBlank(message = "Part name is required")
    @Size(max = 100, message = "Part name must not exceed 100 characters")
    @Schema(description = "Name of the automotive part", example = "Brake Pads", required = true)
    private String name;
    
    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be positive")
    @Digits(integer = 8, fraction = 2, message = "Cost must have at most 8 digits before decimal and 2 after")
    @Schema(description = "Cost of the part", example = "120.00", required = true)
    private BigDecimal cost;
    
    @Pattern(regexp = "^https?://.*", message = "Invoice image URL must be a valid HTTP/HTTPS URL")
    @Schema(description = "URL to the invoice image", example = "https://example.com/invoice.jpg")
    private String invoiceImageUrl;
    
    @NotNull(message = "Purchase date is required")
    @PastOrPresent(message = "Purchase date cannot be in the future")
    @Schema(description = "Date when the part was purchased", example = "2023-11-30", required = true)
    private LocalDate purchaseDate;
} 