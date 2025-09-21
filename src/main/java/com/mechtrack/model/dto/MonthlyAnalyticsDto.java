package com.mechtrack.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.YearMonth;

@Schema(description = "Monthly analytics showing income, expenses, and profit")
public record MonthlyAnalyticsDto(
    @Schema(description = "Month and year for this analytics period", example = "2023-12")
    YearMonth month,
    
    @Schema(description = "Total income from jobs in this month", example = "2150.00")
    BigDecimal totalIncome,
    
    @Schema(description = "Total expenses from parts purchased in this month", example = "650.00")
    BigDecimal totalExpenses,
    
    @Schema(description = "Net profit (income - expenses) for this month", example = "1500.00")
    BigDecimal netProfit,
    
    @Schema(description = "Number of jobs completed in this month", example = "8")
    Integer jobCount,
    
    @Schema(description = "Number of parts purchased in this month", example = "15")
    Integer partCount
) {} 