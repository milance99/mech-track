package com.mechtrack.model.dto;

import com.mechtrack.model.enums.JobType;
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
@Schema(description = "Request payload for creating or updating a job")
public class CreateJobRequest {
    
    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_.]+$", message = "Customer name contains invalid characters")
    @Schema(description = "Name of the customer", example = "John Smith")
    private String customerName;
    
    @NotBlank(message = "Car model is required")
    @Size(min = 2, max = 100, message = "Car model must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_.]+$", message = "Car model contains invalid characters")
    @Schema(description = "Car model and year", example = "Toyota Camry 2018")
    private String carModel;
    
    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 1000, message = "Description must be between 5 and 1000 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_.,!?()]+$", message = "Description contains invalid characters")
    @Schema(description = "Detailed description of the repair work", example = "Oil change and brake inspection")
    private String description;
    
    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    @Schema(description = "Date when the job was performed", example = "2023-12-01")
    private LocalDate date;
    
    @NotNull(message = "Income is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Income must be positive")
    @Digits(integer = 8, fraction = 2, message = "Income must have at most 8 digits before decimal and 2 after")
    @Schema(description = "Total income from the job", example = "150.00")
    private BigDecimal income;
    
    @NotNull(message = "Job type is required")
    @Schema(description = "Type of automotive service", example = "OIL_CHANGE")
    private JobType type;
} 
