package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Part information with job association")
public class PartDto {
    
    @Schema(description = "Unique identifier for the part", example = "650e8400-e29b-41d4-a716-446655440001")
    private UUID id;
    
    @Schema(description = "Name of the automotive part", example = "Brake Pads")
    private String name;
    
    @Schema(description = "Cost of the part", example = "120.00")
    private BigDecimal cost;
    
    @Schema(description = "URL to the invoice image", example = "https://example.com/invoice.jpg")
    private String invoiceImageUrl;
    
    @Schema(description = "Date when the part was purchased", example = "2023-11-30")
    private LocalDate purchaseDate;
    
    @Schema(description = "ID of the job this part is associated with", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID jobId;
} 