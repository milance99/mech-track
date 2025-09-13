package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Job information including all associated parts")
public class JobDto {
    
    @Schema(description = "Unique identifier for the job", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID id;
    
    @Schema(description = "Name of the customer", example = "John Smith")
    private String customerName;
    
    @Schema(description = "Car model and year", example = "Toyota Camry 2018")
    private String carModel;
    
    @Schema(description = "Detailed description of the repair work", example = "Oil change and brake inspection")
    private String description;
    
    @Schema(description = "Date when the job was performed", example = "2023-12-01")
    private LocalDate date;
    
    @Schema(description = "Total income from the job", example = "150.00")
    private BigDecimal income;
    
    @Schema(description = "List of parts used in this job")
    private List<PartDto> parts;
} 