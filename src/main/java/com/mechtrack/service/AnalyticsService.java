package com.mechtrack.service;

import com.mechtrack.model.dto.MonthlyAnalyticsDto;
import com.mechtrack.model.entity.Job;
import com.mechtrack.model.entity.Part;
import com.mechtrack.repository.JobRepository;
import com.mechtrack.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AnalyticsService {

    private final JobRepository jobRepository;
    private final PartRepository partRepository;

    /**
     * Get monthly analytics for a specific date range
     * @param startMonth Start month (inclusive)
     * @param endMonth End month (inclusive)
     * @return List of monthly analytics sorted by month
     */
    public List<MonthlyAnalyticsDto> getMonthlyAnalytics(YearMonth startMonth, YearMonth endMonth) {
        log.info("Calculating monthly analytics from {} to {}", startMonth, endMonth);
        
        LocalDate startDate = startMonth.atDay(1);
        LocalDate endDate = endMonth.atEndOfMonth();
        
        // Fetch all jobs and parts in the date range
        List<Job> jobs = jobRepository.findByDateBetween(startDate, endDate);
        List<Part> parts = partRepository.findByPurchaseDateBetween(startDate, endDate);
        
        // Group jobs by month
        Map<YearMonth, List<Job>> jobsByMonth = jobs.stream()
                .collect(Collectors.groupingBy(job -> YearMonth.from(job.getDate())));
        
        // Group parts by month
        Map<YearMonth, List<Part>> partsByMonth = parts.stream()
                .collect(Collectors.groupingBy(part -> YearMonth.from(part.getPurchaseDate())));
        
        // Generate analytics for each month in the range
        return generateMonthlyAnalytics(startMonth, endMonth, jobsByMonth, partsByMonth);
    }

    /**
     * Get monthly analytics for the last N months
     * @param months Number of months to include (default: 12)
     * @return List of monthly analytics sorted by month
     */
    public List<MonthlyAnalyticsDto> getMonthlyAnalytics(int months) {
        YearMonth endMonth = YearMonth.now();
        YearMonth startMonth = endMonth.minusMonths(months - 1);
        
        return getMonthlyAnalytics(startMonth, endMonth);
    }

    /**
     * Get monthly analytics for the last 12 months
     * @return List of monthly analytics sorted by month
     */
    public List<MonthlyAnalyticsDto> getMonthlyAnalytics() {
        return getMonthlyAnalytics(12);
    }

    /**
     * Get analytics for a specific month
     * @param month The specific month
     * @return Monthly analytics for that month
     */
    public MonthlyAnalyticsDto getMonthlyAnalytics(YearMonth month) {
        List<MonthlyAnalyticsDto> analytics = getMonthlyAnalytics(month, month);
        return analytics.isEmpty() ? createEmptyAnalytics(month) : analytics.get(0);
    }

    private List<MonthlyAnalyticsDto> generateMonthlyAnalytics(
            YearMonth startMonth, 
            YearMonth endMonth,
            Map<YearMonth, List<Job>> jobsByMonth,
            Map<YearMonth, List<Part>> partsByMonth) {
        
        List<MonthlyAnalyticsDto> result = new java.util.ArrayList<>();
        YearMonth current = startMonth;
        
        while (!current.isAfter(endMonth)) {
            result.add(calculateMonthlyAnalytics(current, jobsByMonth, partsByMonth));
            current = current.plusMonths(1);
        }
        
        return result;
    }

    private MonthlyAnalyticsDto calculateMonthlyAnalytics(
            YearMonth month,
            Map<YearMonth, List<Job>> jobsByMonth,
            Map<YearMonth, List<Part>> partsByMonth) {
        
        List<Job> monthJobs = jobsByMonth.getOrDefault(month, List.of());
        List<Part> monthParts = partsByMonth.getOrDefault(month, List.of());
        
        // Calculate totals
        BigDecimal totalIncome = monthJobs.stream()
                .map(Job::getIncome)
                .filter(income -> income != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalExpenses = monthParts.stream()
                .map(Part::getCost)
                .filter(cost -> cost != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal netProfit = totalIncome.subtract(totalExpenses);
        
        MonthlyAnalyticsDto analytics = new MonthlyAnalyticsDto();
        analytics.setMonth(month);
        analytics.setTotalIncome(totalIncome);
        analytics.setTotalExpenses(totalExpenses);
        analytics.setNetProfit(netProfit);
        analytics.setJobCount(monthJobs.size());
        analytics.setPartCount(monthParts.size());
        
        return analytics;
    }

    private MonthlyAnalyticsDto createEmptyAnalytics(YearMonth month) {
        return new MonthlyAnalyticsDto(
                month,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                0,
                0
        );
    }
} 