package com.mechtrack.service;

import com.mechtrack.AbstractMechtrackTest;
import com.mechtrack.model.dto.MonthlyAnalyticsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static com.mechtrack.provider.JobTestDataProvider.createJobRequest;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class AnalyticsServiceTest extends AbstractMechtrackTest {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private JobService jobService;

    @Test
    @DisplayName("Should calculate monthly analytics correctly")
    void shouldCalculateMonthlyAnalyticsCorrectly() {
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        
        jobService.createJob(createJobRequest("Customer 1", "Car 1", "Service 1", testDate, new BigDecimal("100.00")));
        jobService.createJob(createJobRequest("Customer 2", "Car 2", "Service 2", testDate, new BigDecimal("200.00")));
        jobService.createJob(createJobRequest("Customer 3", "Car 3", "Service 3", testDate.plusDays(10), new BigDecimal("150.00")));
        
        jobService.createJob(createJobRequest("Customer 4", "Car 4", "Service 4", testDate.plusMonths(1), new BigDecimal("300.00")));

        List<MonthlyAnalyticsDto> analytics = analyticsService.getMonthlyAnalytics(YearMonth.of(2024, 1), YearMonth.of(2024, 2));

        assertThat(analytics).hasSize(2);
        
        MonthlyAnalyticsDto january = analytics.stream()
                .filter(a -> a.getMonth().equals(YearMonth.of(2024, 1)))
                .findFirst()
                .orElseThrow();
        
        assertThat(january.getJobCount()).isEqualTo(3);
        assertThat(january.getTotalIncome()).isEqualByComparingTo("450.00");
        
        MonthlyAnalyticsDto february = analytics.stream()
                .filter(a -> a.getMonth().equals(YearMonth.of(2024, 2)))
                .findFirst()
                .orElseThrow();
        
        assertThat(february.getJobCount()).isEqualTo(1);
        assertThat(february.getTotalIncome()).isEqualByComparingTo("300.00");
    }

    @Test
    @DisplayName("Should return all months with zero values for year with no jobs")
    void shouldReturnAllMonthsWithZeroValuesForYearWithNoJobs() {
        List<MonthlyAnalyticsDto> analytics = analyticsService.getMonthlyAnalytics(YearMonth.of(2020, 1), YearMonth.of(2020, 12));

        assertThat(analytics).hasSize(12);
        assertThat(analytics).allSatisfy(monthlyAnalytics -> {
            assertThat(monthlyAnalytics.getTotalIncome()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(monthlyAnalytics.getTotalExpenses()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(monthlyAnalytics.getNetProfit()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(monthlyAnalytics.getJobCount()).isEqualTo(0);
            assertThat(monthlyAnalytics.getPartCount()).isEqualTo(0);
        });
    }

    @Test
    @DisplayName("Should handle year with jobs in different months")
    void shouldHandleYearWithJobsInDifferentMonths() {
        LocalDate january = LocalDate.of(2024, 1, 15);
        LocalDate march = LocalDate.of(2024, 3, 10);
        LocalDate december = LocalDate.of(2024, 12, 20);
        
        jobService.createJob(createJobRequest("Customer 1", "Car 1", "Service 1", january, new BigDecimal("100.00")));
        jobService.createJob(createJobRequest("Customer 2", "Car 2", "Service 2", march, new BigDecimal("200.00")));
        jobService.createJob(createJobRequest("Customer 3", "Car 3", "Service 3", december, new BigDecimal("300.00")));

        List<MonthlyAnalyticsDto> analytics = analyticsService.getMonthlyAnalytics(YearMonth.of(2024, 1), YearMonth.of(2024, 12));

        assertThat(analytics).hasSize(12);
        assertThat(analytics.stream().filter(a -> a.getJobCount() > 0))
                .extracting(MonthlyAnalyticsDto::getMonth)
                .containsExactlyInAnyOrder(YearMonth.of(2024, 1), YearMonth.of(2024, 3), YearMonth.of(2024, 12));
    }

    @Test
    @DisplayName("Should calculate correct totals for multiple jobs in same month")
    void shouldCalculateCorrectTotalsForMultipleJobsInSameMonth() {
        LocalDate testDate = LocalDate.of(2024, 6, 15);
        
        jobService.createJob(createJobRequest("Customer 1", "Car 1", "Service 1", testDate, new BigDecimal("100.00")));
        jobService.createJob(createJobRequest("Customer 2", "Car 2", "Service 2", testDate.plusDays(5), new BigDecimal("200.00")));
        jobService.createJob(createJobRequest("Customer 3", "Car 3", "Service 3", testDate.plusDays(10), new BigDecimal("300.00")));

        List<MonthlyAnalyticsDto> analytics = analyticsService.getMonthlyAnalytics(YearMonth.of(2024, 6), YearMonth.of(2024, 6));

        MonthlyAnalyticsDto june = analytics.stream()
                .filter(a -> a.getMonth().equals(YearMonth.of(2024, 6)))
                .findFirst()
                .orElseThrow();
        
        assertThat(june.getJobCount()).isEqualTo(3);
        assertThat(june.getTotalIncome()).isEqualByComparingTo("600.00");
    }

    @Test
    @DisplayName("Should handle zero income jobs")
    void shouldHandleZeroIncomeJobs() {
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        
        jobService.createJob(createJobRequest("Customer 1", "Car 1", "Free Service", testDate, BigDecimal.ZERO));
        jobService.createJob(createJobRequest("Customer 2", "Car 2", "Paid Service", testDate, new BigDecimal("100.00")));

        List<MonthlyAnalyticsDto> analytics = analyticsService.getMonthlyAnalytics(YearMonth.of(2024, 1), YearMonth.of(2024, 1));

        MonthlyAnalyticsDto january = analytics.stream()
                .filter(a -> a.getMonth().equals(YearMonth.of(2024, 1)))
                .findFirst()
                .orElseThrow();
        
        assertThat(january.getJobCount()).isEqualTo(2);
        assertThat(january.getTotalIncome()).isEqualByComparingTo("100.00");
    }
}
