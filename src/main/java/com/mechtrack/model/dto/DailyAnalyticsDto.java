package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Daily analytics showing income, expenses, and profit for a specific day")
public class DailyAnalyticsDto {
    
    @Schema(description = "Date for this analytics period", example = "2025-09-20")
    private LocalDate date;
    
    @Schema(description = "Total income from jobs completed on this day", example = "450.00")
    private BigDecimal totalIncome;
    
    @Schema(description = "Total expenses from parts purchased on this day", example = "120.00")
    private BigDecimal totalExpenses;
    
    @Schema(description = "Net profit (income - expenses) for this day", example = "330.00")
    private BigDecimal netProfit;
    
    @Schema(description = "Number of jobs completed on this day", example = "2")
    private Integer jobCount;
    
    @Schema(description = "Number of parts purchased on this day", example = "5")
    private Integer partCount;
}

