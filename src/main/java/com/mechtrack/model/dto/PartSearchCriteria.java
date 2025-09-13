package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Search criteria for filtering parts with multiple optional parameters")
public class PartSearchCriteria {

    @Schema(description = "Filter by part name (case-insensitive, partial match)", 
            example = "brake pad")
    private String name;

    @Schema(description = "Filter by associated job ID", 
            example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID jobId;

    @Schema(description = "Filter parts with cost greater than or equal to this amount", 
            example = "10.00")
    private BigDecimal minCost;

    @Schema(description = "Filter parts with cost less than or equal to this amount", 
            example = "200.00")
    private BigDecimal maxCost;

    @Schema(description = "Filter parts purchased from this date onwards (inclusive)", 
            example = "2023-01-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startPurchaseDate;

    @Schema(description = "Filter parts purchased up to this date (inclusive)", 
            example = "2023-12-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endPurchaseDate;

    @Schema(description = "Filter by customer name associated with the job (case-insensitive, partial match)", 
            example = "John Smith")
    private String customerName;

    @Schema(description = "Filter by car model associated with the job (case-insensitive, partial match)", 
            example = "Toyota")
    private String carModel;
} 