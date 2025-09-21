package com.mechtrack.provider;

import com.mechtrack.model.dto.CreateJobRequest;
import com.mechtrack.model.dto.JobDto;
import com.mechtrack.model.dto.JobSearchCriteria;
import com.mechtrack.model.enums.JobStatus;
import com.mechtrack.model.enums.JobType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Test data provider for Job-related DTOs
 */
public class JobTestDataProvider {

    public static CreateJobRequest createJobRequest() {
        CreateJobRequest request = new CreateJobRequest();
        request.setCustomerName("John Smith");
        request.setCarModel("Toyota Camry 2020");
        request.setDescription("Oil change and brake inspection");
        request.setDate(LocalDate.of(2023, 12, 1));
        request.setIncome(new BigDecimal("150.00"));
        request.setType(JobType.GENERAL_MAINTENANCE);
        return request;
    }

    public static CreateJobRequest createJobRequest(String customerName, String carModel, String description, LocalDate date, BigDecimal income) {
        CreateJobRequest request = new CreateJobRequest();
        request.setCustomerName(customerName);
        request.setCarModel(carModel);
        request.setDescription(description);
        request.setDate(date);
        request.setIncome(income);
        request.setType(JobType.GENERAL_MAINTENANCE);
        return request;
    }

    public static JobDto createJobDto() {
        return new JobDto(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
                "John Smith",
                "Toyota Camry 2020",
                "Oil change and brake inspection",
                LocalDate.of(2023, 12, 1),
                new BigDecimal("150.00"),
                JobStatus.WAITING,
                JobType.GENERAL_MAINTENANCE,
                List.of()
        );
    }

    public static JobDto createJobDto(UUID id, String customerName, String carModel) {
        return new JobDto(
                id,
                customerName,
                carModel,
                "Standard repair work",
                LocalDate.now(),
                new BigDecimal("100.00"),
                JobStatus.WAITING,
                JobType.GENERAL_MAINTENANCE,
                List.of()
        );
    }

    public static JobSearchCriteria createJobSearchCriteria() {
        JobSearchCriteria criteria = new JobSearchCriteria();
        criteria.setCustomerName("John");
        criteria.setCarModel("Toyota");
        criteria.setDescription("brake");
        criteria.setStartDate(LocalDate.of(2023, 1, 1));
        criteria.setEndDate(LocalDate.of(2023, 12, 31));
        criteria.setMinIncome(new BigDecimal("100.00"));
        criteria.setMaxIncome(new BigDecimal("1000.00"));
        criteria.setPartName("brake pad");
        criteria.setMinPartCost(new BigDecimal("50.00"));
        criteria.setMaxPartCost(new BigDecimal("500.00"));
        criteria.setStatus(JobStatus.IN_PROGRESS);
        criteria.setType(JobType.BRAKE_SERVICE);
        return criteria;
    }

    public static JobSearchCriteria createJobSearchCriteria(String customerName, String carModel) {
        JobSearchCriteria criteria = new JobSearchCriteria();
        criteria.setCustomerName(customerName);
        criteria.setCarModel(carModel);
        return criteria;
    }

    public static CreateJobRequest createInvalidJobRequest() {
        CreateJobRequest request = new CreateJobRequest();
        request.setCustomerName(""); // Invalid: empty customer name
        request.setCarModel(""); // Invalid: empty car model
        request.setDescription(""); // Invalid: empty description
        request.setDate(null); // Invalid: null date
        request.setIncome(null); // Invalid: null income
        request.setType(null); // Invalid: null type
        return request;
    }

    public static CreateJobRequest createJobRequestWithNegativeIncome() {
        CreateJobRequest request = new CreateJobRequest();
        request.setCustomerName("Test Customer");
        request.setCarModel("Test Car");
        request.setDescription("Test Description");
        request.setDate(LocalDate.now());
        request.setIncome(new BigDecimal("-100.00")); // Invalid: negative income
        request.setType(JobType.GENERAL_MAINTENANCE);
        return request;
    }

    public static CreateJobRequest createJobRequestWithFutureDate() {
        CreateJobRequest request = new CreateJobRequest();
        request.setCustomerName("Test Customer");
        request.setCarModel("Test Car");
        request.setDescription("Test Description");
        request.setDate(LocalDate.now().plusDays(1)); // Invalid: future date
        request.setIncome(new BigDecimal("100.00"));
        request.setType(JobType.GENERAL_MAINTENANCE);
        return request;
    }
} 