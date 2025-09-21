package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Part information with job association")
public record PartDto(
    @Schema(description = "Unique identifier for the part", example = "650e8400-e29b-41d4-a716-446655440001")
    UUID id,
    
    @Schema(description = "Name of the automotive part", example = "Brake Pads")
    String name,
    
    @Schema(description = "Cost of the part", example = "120.00")
    BigDecimal cost,
    
    @Schema(description = "URL to the invoice image", example = "https://example.com/invoice.jpg")
    String invoiceImageUrl,
    
    @Schema(description = "Date when the part was purchased", example = "2023-11-30")
    LocalDate purchaseDate,
    
    @Schema(description = "ID of the job this part is associated with", example = "550e8400-e29b-41d4-a716-446655440001")
    UUID jobId
) {} 