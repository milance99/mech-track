package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Search criteria for filtering jobs with multiple optional parameters")
public class JobSearchCriteria {

    @Schema(description = "Filter by customer name (case-insensitive, partial match)", 
            example = "John Smith")
    private String customerName;

    @Schema(description = "Filter by car model (case-insensitive, partial match)", 
            example = "Toyota Camry")
    private String carModel;

    @Schema(description = "Filter by job description (case-insensitive, partial match)", 
            example = "brake repair")
    private String description;

    @Schema(description = "Filter jobs from this date onwards (inclusive)", 
            example = "2023-01-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Schema(description = "Filter jobs up to this date (inclusive)", 
            example = "2023-12-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @Schema(description = "Filter jobs with income greater than or equal to this amount", 
            example = "100.00")
    private BigDecimal minIncome;

    @Schema(description = "Filter jobs with income less than or equal to this amount", 
            example = "1000.00")
    private BigDecimal maxIncome;

    @Schema(description = "Filter jobs that contain parts with this name (case-insensitive, partial match)", 
            example = "brake pad")
    private String partName;

    @Schema(description = "Filter jobs with total part cost greater than or equal to this amount", 
            example = "50.00")
    private BigDecimal minPartCost;

    @Schema(description = "Filter jobs with total part cost less than or equal to this amount", 
            example = "500.00")
    private BigDecimal maxPartCost;
} 