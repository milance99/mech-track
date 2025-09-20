package com.mechtrack.service;

import com.mechtrack.AbstractMechtrackTest;
import com.mechtrack.model.dto.DailyAnalyticsDto;
import com.mechtrack.model.dto.MonthlyAnalyticsDto;
import com.mechtrack.model.enums.TimeInterval;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("Should calculate daily analytics correctly for 7 days interval")
    void shouldCalculateDailyAnalyticsCorrectlyFor7DaysInterval() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate twoDaysAgo = today.minusDays(2);
        
        jobService.createJob(createJobRequest("Customer 1", "Car 1", "Service 1", today, new BigDecimal("100.00")));
        jobService.createJob(createJobRequest("Customer 2", "Car 2", "Service 2", yesterday, new BigDecimal("200.00")));
        jobService.createJob(createJobRequest("Customer 3", "Car 3", "Service 3", twoDaysAgo, new BigDecimal("150.00")));

        List<DailyAnalyticsDto> analytics = analyticsService.getDailyAnalytics(TimeInterval.SEVEN_DAYS);

        assertThat(analytics).hasSize(7);
        
        for (int i = 1; i < analytics.size(); i++) {
            assertThat(analytics.get(i).getDate()).isAfter(analytics.get(i-1).getDate());
        }
        
        DailyAnalyticsDto todayAnalytics = analytics.stream()
                .filter(a -> a.getDate().equals(today))
                .findFirst()
                .orElseThrow();
        assertThat(todayAnalytics.getJobCount()).isEqualTo(1);
        assertThat(todayAnalytics.getTotalIncome()).isEqualByComparingTo("100.00");
        
        DailyAnalyticsDto yesterdayAnalytics = analytics.stream()
                .filter(a -> a.getDate().equals(yesterday))
                .findFirst()
                .orElseThrow();
        assertThat(yesterdayAnalytics.getJobCount()).isEqualTo(1);
        assertThat(yesterdayAnalytics.getTotalIncome()).isEqualByComparingTo("200.00");
    }

    @Test
    @DisplayName("Should calculate daily analytics correctly for 1 month interval")
    void shouldCalculateDailyAnalyticsCorrectlyFor1MonthInterval() {
        LocalDate today = LocalDate.now();
        
        jobService.createJob(createJobRequest("Customer 1", "Car 1", "Service 1", today, new BigDecimal("100.00")));
        jobService.createJob(createJobRequest("Customer 2", "Car 2", "Service 2", today.minusDays(15), new BigDecimal("200.00")));

        List<DailyAnalyticsDto> analytics = analyticsService.getDailyAnalytics(TimeInterval.ONE_MONTH);

        assertThat(analytics).hasSize(30);
        
        long daysWithData = analytics.stream()
                .mapToLong(DailyAnalyticsDto::getJobCount)
                .filter(count -> count > 0)
                .count();
        assertThat(daysWithData).isEqualTo(2);
        
        long daysWithZeroIncome = analytics.stream()
                .filter(a -> a.getTotalIncome().equals(BigDecimal.ZERO))
                .count();
        assertThat(daysWithZeroIncome).isEqualTo(28);
    }

    @Test
    @DisplayName("Should calculate daily analytics correctly for 3 months interval")
    void shouldCalculateDailyAnalyticsCorrectlyFor3MonthsInterval() {
        List<DailyAnalyticsDto> analytics = analyticsService.getDailyAnalytics(TimeInterval.THREE_MONTHS);

        assertThat(analytics).hasSize(90);
        
        assertThat(analytics).allSatisfy(dailyAnalytics -> {
            assertThat(dailyAnalytics.getTotalIncome()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(dailyAnalytics.getTotalExpenses()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(dailyAnalytics.getNetProfit()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(dailyAnalytics.getJobCount()).isEqualTo(0);
            assertThat(dailyAnalytics.getPartCount()).isEqualTo(0);
        });
    }

    @Test
    @DisplayName("Should calculate daily analytics for custom date range")
    void shouldCalculateDailyAnalyticsForCustomDateRange() {
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 5);
        LocalDate testDate = LocalDate.of(2024, 6, 3);
        
        jobService.createJob(createJobRequest("Customer 1", "Car 1", "Service 1", testDate, new BigDecimal("150.00")));
        jobService.createJob(createJobRequest("Customer 2", "Car 2", "Service 2", testDate, new BigDecimal("250.00")));

        List<DailyAnalyticsDto> analytics = analyticsService.getDailyAnalytics(startDate, endDate);

        assertThat(analytics).hasSize(5);
        
        DailyAnalyticsDto testDayAnalytics = analytics.stream()
                .filter(a -> a.getDate().equals(testDate))
                .findFirst()
                .orElseThrow();
        
        assertThat(testDayAnalytics.getJobCount()).isEqualTo(2);
        assertThat(testDayAnalytics.getTotalIncome()).isEqualByComparingTo("400.00");
        assertThat(testDayAnalytics.getNetProfit()).isEqualByComparingTo("400.00"); // No expenses
        
        long daysWithZeroJobs = analytics.stream()
                .filter(a -> !a.getDate().equals(testDate))
                .mapToLong(DailyAnalyticsDto::getJobCount)
                .filter(count -> count == 0)
                .count();
        assertThat(daysWithZeroJobs).isEqualTo(4);
    }

    @Test
    @DisplayName("Should get daily analytics for specific date")
    void shouldGetDailyAnalyticsForSpecificDate() {
        LocalDate testDate = LocalDate.of(2024, 7, 15);
        
        jobService.createJob(createJobRequest("Customer 1", "Car 1", "Service 1", testDate, new BigDecimal("300.00")));
        jobService.createJob(createJobRequest("Customer 2", "Car 2", "Service 2", testDate, new BigDecimal("200.00")));

        DailyAnalyticsDto analytics = analyticsService.getDailyAnalytics(testDate);

        assertThat(analytics.getDate()).isEqualTo(testDate);
        assertThat(analytics.getJobCount()).isEqualTo(2);
        assertThat(analytics.getTotalIncome()).isEqualByComparingTo("500.00");
        assertThat(analytics.getTotalExpenses()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(analytics.getNetProfit()).isEqualByComparingTo("500.00");
        assertThat(analytics.getPartCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return empty analytics for date with no data")
    void shouldReturnEmptyAnalyticsForDateWithNoData() {
        LocalDate testDate = LocalDate.of(2020, 1, 1);

        DailyAnalyticsDto analytics = analyticsService.getDailyAnalytics(testDate);

        assertThat(analytics.getDate()).isEqualTo(testDate);
        assertThat(analytics.getJobCount()).isEqualTo(0);
        assertThat(analytics.getTotalIncome()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(analytics.getTotalExpenses()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(analytics.getNetProfit()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(analytics.getPartCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle multiple jobs on same day correctly")
    void shouldHandleMultipleJobsOnSameDayCorrectly() {
        LocalDate testDate = LocalDate.of(2024, 8, 20);
        
        jobService.createJob(createJobRequest("Customer 1", "Car 1", "Service 1", testDate, new BigDecimal("100.00")));
        jobService.createJob(createJobRequest("Customer 2", "Car 2", "Service 2", testDate, new BigDecimal("200.00")));
        jobService.createJob(createJobRequest("Customer 3", "Car 3", "Service 3", testDate, new BigDecimal("300.00")));
        jobService.createJob(createJobRequest("Customer 4", "Car 4", "Free Service", testDate, BigDecimal.ZERO));

        DailyAnalyticsDto analytics = analyticsService.getDailyAnalytics(testDate);

        assertThat(analytics.getJobCount()).isEqualTo(4);
        assertThat(analytics.getTotalIncome()).isEqualByComparingTo("600.00");
        assertThat(analytics.getNetProfit()).isEqualByComparingTo("600.00");
    }

    @Test
    @DisplayName("Should validate TimeInterval enum fromCode method")
    void shouldValidateTimeIntervalEnumFromCodeMethod() {
        assertThat(TimeInterval.fromCode("7d")).isEqualTo(TimeInterval.SEVEN_DAYS);
        assertThat(TimeInterval.fromCode("1m")).isEqualTo(TimeInterval.ONE_MONTH);
        assertThat(TimeInterval.fromCode("3m")).isEqualTo(TimeInterval.THREE_MONTHS);
        
        assertThat(TimeInterval.fromCode("7D")).isEqualTo(TimeInterval.SEVEN_DAYS);
        assertThat(TimeInterval.fromCode("1M")).isEqualTo(TimeInterval.ONE_MONTH);
        
        assertThatThrownBy(() -> TimeInterval.fromCode("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid time interval code: invalid");
                
        assertThatThrownBy(() -> TimeInterval.fromCode("6m"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Valid codes are: 7d, 1m, 3m");
    }

    @Test
    @DisplayName("Should verify TimeInterval enum properties")
    void shouldVerifyTimeIntervalEnumProperties() {
        assertThat(TimeInterval.SEVEN_DAYS.getCode()).isEqualTo("7d");
        assertThat(TimeInterval.SEVEN_DAYS.getDays()).isEqualTo(7);
        assertThat(TimeInterval.SEVEN_DAYS.toString()).isEqualTo("7d");
        
        assertThat(TimeInterval.ONE_MONTH.getCode()).isEqualTo("1m");
        assertThat(TimeInterval.ONE_MONTH.getDays()).isEqualTo(30);
        assertThat(TimeInterval.ONE_MONTH.toString()).isEqualTo("1m");
        
        assertThat(TimeInterval.THREE_MONTHS.getCode()).isEqualTo("3m");
        assertThat(TimeInterval.THREE_MONTHS.getDays()).isEqualTo(90);
        assertThat(TimeInterval.THREE_MONTHS.toString()).isEqualTo("3m");
    }
}
