package com.mechtrack.model.dto;

import com.mechtrack.model.enums.JobStatus;
import com.mechtrack.model.enums.JobType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(description = "Job information including all associated parts")
public record JobDto(
    @Schema(description = "Unique identifier for the job", example = "550e8400-e29b-41d4-a716-446655440001")
    UUID id,
    
    @Schema(description = "Name of the customer", example = "John Smith")
    String customerName,
    
    @Schema(description = "Car model and year", example = "Toyota Camry 2018")
    String carModel,
    
    @Schema(description = "Detailed description of the repair work", example = "Oil change and brake inspection")
    String description,
    
    @Schema(description = "Date when the job was performed", example = "2023-12-01")
    LocalDate date,
    
    @Schema(description = "Total income from the job", example = "150.00")
    BigDecimal income,
    
    @Schema(description = "Current status of the job", example = "IN_PROGRESS")
    JobStatus status,
    
    @Schema(description = "Type of automotive service", example = "OIL_CHANGE")
    JobType type,
    
    @Schema(description = "List of parts used in this job")
    List<PartDto> parts
) {} 