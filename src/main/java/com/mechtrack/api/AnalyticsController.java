package com.mechtrack.api;

import com.mechtrack.model.dto.MonthlyAnalyticsDto;
import com.mechtrack.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = {"${app.cors.allowed-origins:http://localhost:3000}"})
@Tag(name = "Analytics", description = "Financial analytics and reporting operations")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/monthly")
    @Operation(
        summary = "Get monthly analytics",
        description = "Retrieves monthly financial analytics including income, expenses, and profit. Returns data for the last 12 months by default."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Monthly analytics retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MonthlyAnalyticsDto.class),
                examples = @ExampleObject(
                    name = "Monthly Analytics Response",
                    value = """
                    [
                        {
                            "month": "2023-11",
                            "totalIncome": 2150.00,
                            "totalExpenses": 650.00,
                            "netProfit": 1500.00,
                            "jobCount": 8,
                            "partCount": 15
                        },
                        {
                            "month": "2023-12",
                            "totalIncome": 1850.00,
                            "totalExpenses": 420.00,
                            "netProfit": 1430.00,
                            "jobCount": 6,
                            "partCount": 12
                        }
                    ]
                    """
                )
            )
        )
    })
    public ResponseEntity<List<MonthlyAnalyticsDto>> getMonthlyAnalytics(
        @Parameter(description = "Start month in YYYY-MM format", example = "2023-01")
        @RequestParam(value = "start", required = false) 
        @DateTimeFormat(pattern = "yyyy-MM") 
        YearMonth start,
        
        @Parameter(description = "End month in YYYY-MM format", example = "2023-12")
        @RequestParam(value = "end", required = false) 
        @DateTimeFormat(pattern = "yyyy-MM") 
        YearMonth end,
        
        @Parameter(description = "Number of months to include from current month backwards", example = "6")
        @RequestParam(value = "months", required = false, defaultValue = "12") 
        Integer months) {
        
        List<MonthlyAnalyticsDto> analytics;
        
        if (start != null && end != null) {
            analytics = analyticsService.getMonthlyAnalytics(start, end);
        } else {
            analytics = analyticsService.getMonthlyAnalytics(months);
        }
        
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/monthly/{month}")
    @Operation(
        summary = "Get analytics for a specific month",
        description = "Retrieves financial analytics for a specific month and year."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Monthly analytics retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MonthlyAnalyticsDto.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid month format")
    })
    public ResponseEntity<MonthlyAnalyticsDto> getMonthlyAnalytics(
        @Parameter(description = "Month in YYYY-MM format", example = "2023-12")
        @PathVariable("month") 
        @DateTimeFormat(pattern = "yyyy-MM") 
        YearMonth month) {
        
        MonthlyAnalyticsDto analytics = analyticsService.getMonthlyAnalytics(month);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/summary")
    @Operation(
        summary = "Get analytics summary",
        description = "Retrieves a quick summary of financial performance for the last 12 months."
    )
    public ResponseEntity<MonthlyAnalyticsDto> getAnalyticsSummary() {
        List<MonthlyAnalyticsDto> last12Months = analyticsService.getMonthlyAnalytics(12);
        
        // Aggregate the last 12 months
        MonthlyAnalyticsDto summary = last12Months.stream()
                .reduce(new MonthlyAnalyticsDto(
                        YearMonth.now(),
                        java.math.BigDecimal.ZERO,
                        java.math.BigDecimal.ZERO,
                        java.math.BigDecimal.ZERO,
                        0,
                        0
                ), (accumulated, monthly) -> {
                    accumulated.setTotalIncome(accumulated.getTotalIncome().add(monthly.getTotalIncome()));
                    accumulated.setTotalExpenses(accumulated.getTotalExpenses().add(monthly.getTotalExpenses()));
                    accumulated.setNetProfit(accumulated.getNetProfit().add(monthly.getNetProfit()));
                    accumulated.setJobCount(accumulated.getJobCount() + monthly.getJobCount());
                    accumulated.setPartCount(accumulated.getPartCount() + monthly.getPartCount());
                    return accumulated;
                });
        
        return ResponseEntity.ok(summary);
    }
} 
