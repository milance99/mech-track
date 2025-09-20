package com.mechtrack.controller;

import com.mechtrack.AbstractMechtrackMvcTest;
import com.mechtrack.model.dto.DailyAnalyticsDto;
import com.mechtrack.model.enums.TimeInterval;
import com.mechtrack.service.AnalyticsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnalyticsControllerTest extends AbstractMechtrackMvcTest {

    @MockBean
    private AnalyticsService analyticsService;

    @Test
    @DisplayName("Should get daily analytics with default 7d interval")
    void shouldGetDailyAnalyticsWithDefault7dInterval() throws Exception {
        LocalDate today = LocalDate.now();
        List<DailyAnalyticsDto> mockAnalytics = List.of(
            new DailyAnalyticsDto(today.minusDays(1), new BigDecimal("100.00"), new BigDecimal("20.00"), new BigDecimal("80.00"), 1, 2),
            new DailyAnalyticsDto(today, new BigDecimal("200.00"), new BigDecimal("50.00"), new BigDecimal("150.00"), 2, 3)
        );

        when(analyticsService.getDailyAnalytics(TimeInterval.SEVEN_DAYS))
                .thenReturn(mockAnalytics);

        mvc.perform(get("/api/analytics/daily"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].date").value(today.minusDays(1).toString()))
                .andExpect(jsonPath("$[0].totalIncome").value(100.00))
                .andExpect(jsonPath("$[0].totalExpenses").value(20.00))
                .andExpect(jsonPath("$[0].netProfit").value(80.00))
                .andExpect(jsonPath("$[0].jobCount").value(1))
                .andExpect(jsonPath("$[0].partCount").value(2))
                .andExpect(jsonPath("$[1].date").value(today.toString()))
                .andExpect(jsonPath("$[1].totalIncome").value(200.00));
    }

    @Test
    @DisplayName("Should get daily analytics with 1m interval")
    void shouldGetDailyAnalyticsWith1mInterval() throws Exception {
        List<DailyAnalyticsDto> mockAnalytics = List.of(
            new DailyAnalyticsDto(LocalDate.now(), new BigDecimal("300.00"), BigDecimal.ZERO, new BigDecimal("300.00"), 1, 0)
        );

        when(analyticsService.getDailyAnalytics(TimeInterval.ONE_MONTH))
                .thenReturn(mockAnalytics);

        mvc.perform(get("/api/analytics/daily")
                        .param("interval", "1m"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].totalIncome").value(300.00))
                .andExpect(jsonPath("$[0].netProfit").value(300.00));
    }

    @Test
    @DisplayName("Should get daily analytics with 3m interval")
    void shouldGetDailyAnalyticsWith3mInterval() throws Exception {
        List<DailyAnalyticsDto> mockAnalytics = List.of();

        when(analyticsService.getDailyAnalytics(TimeInterval.THREE_MONTHS))
                .thenReturn(mockAnalytics);

        mvc.perform(get("/api/analytics/daily")
                        .param("interval", "3m"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Should get daily analytics with custom date range")
    void shouldGetDailyAnalyticsWithCustomDateRange() throws Exception {
        LocalDate startDate = LocalDate.of(2024, 9, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 5);
        
        List<DailyAnalyticsDto> mockAnalytics = List.of(
            new DailyAnalyticsDto(LocalDate.of(2024, 9, 3), new BigDecimal("500.00"), new BigDecimal("100.00"), new BigDecimal("400.00"), 2, 1)
        );

        when(analyticsService.getDailyAnalytics(startDate, endDate))
                .thenReturn(mockAnalytics);

        mvc.perform(get("/api/analytics/daily")
                        .param("start", "2024-09-01")
                        .param("end", "2024-09-05"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].date").value("2024-09-03"))
                .andExpect(jsonPath("$[0].totalIncome").value(500.00))
                .andExpect(jsonPath("$[0].netProfit").value(400.00));
    }

    @Test
    @DisplayName("Should get daily analytics for specific date")
    void shouldGetDailyAnalyticsForSpecificDate() throws Exception {
        LocalDate testDate = LocalDate.of(2024, 9, 20);
        DailyAnalyticsDto mockAnalytics = new DailyAnalyticsDto(
            testDate, 
            new BigDecimal("750.00"), 
            new BigDecimal("150.00"), 
            new BigDecimal("600.00"), 
            3, 
            2
        );

        when(analyticsService.getDailyAnalytics(testDate))
                .thenReturn(mockAnalytics);

        mvc.perform(get("/api/analytics/daily/2024-09-20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value("2024-09-20"))
                .andExpect(jsonPath("$.totalIncome").value(750.00))
                .andExpect(jsonPath("$.totalExpenses").value(150.00))
                .andExpect(jsonPath("$.netProfit").value(600.00))
                .andExpect(jsonPath("$.jobCount").value(3))
                .andExpect(jsonPath("$.partCount").value(2));
    }

    @Test
    @DisplayName("Should return 400 for invalid interval code")
    void shouldReturn400ForInvalidIntervalCode() throws Exception {
        when(analyticsService.getDailyAnalytics(any(TimeInterval.class)))
                .thenThrow(new IllegalArgumentException("Invalid time interval code: 6m. Valid codes are: 7d, 1m, 3m"));

        mvc.perform(get("/api/analytics/daily")
                        .param("interval", "6m"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid time interval code: 6m. Valid codes are: 7d, 1m, 3m"));
    }

    @Test
    @DisplayName("Should handle invalid date format")
    void shouldHandleInvalidDateFormat() throws Exception {
        mvc.perform(get("/api/analytics/daily/invalid-date"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle invalid date range parameters")
    void shouldHandleInvalidDateRangeParameters() throws Exception {
        mvc.perform(get("/api/analytics/daily")
                        .param("start", "invalid-date")
                        .param("end", "2024-09-20"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should prioritize custom date range over interval parameter")
    void shouldPrioritizeCustomDateRangeOverIntervalParameter() throws Exception {
        LocalDate startDate = LocalDate.of(2024, 9, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 5);
        
        List<DailyAnalyticsDto> mockAnalytics = List.of(
            new DailyAnalyticsDto(LocalDate.of(2024, 9, 3), new BigDecimal("100.00"), BigDecimal.ZERO, new BigDecimal("100.00"), 1, 0)
        );

        when(analyticsService.getDailyAnalytics(startDate, endDate))
                .thenReturn(mockAnalytics);

        // Even though we provide interval=3m, the custom date range should be used
        mvc.perform(get("/api/analytics/daily")
                        .param("interval", "3m")
                        .param("start", "2024-09-01")
                        .param("end", "2024-09-05"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        // Verify that the service was called with custom date range, not the interval
        // (The interval parameter should be ignored when both start and end are provided)
    }

    @Test
    @DisplayName("Should handle case insensitive interval codes")
    void shouldHandleCaseInsensitiveIntervalCodes() throws Exception {
        List<DailyAnalyticsDto> mockAnalytics = List.of();

        when(analyticsService.getDailyAnalytics(TimeInterval.ONE_MONTH))
                .thenReturn(mockAnalytics);

        mvc.perform(get("/api/analytics/daily")
                        .param("interval", "1M"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
