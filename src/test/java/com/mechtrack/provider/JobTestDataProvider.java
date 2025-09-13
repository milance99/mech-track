package com.mechtrack.provider;

import com.mechtrack.model.dto.CreateJobRequest;
import com.mechtrack.model.dto.JobDto;
import com.mechtrack.model.dto.JobSearchCriteria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Test data provider for Job-related DTOs
 */
public class JobTestDataProvider {

    public static CreateJobRequest createJobRequest() {
        return new CreateJobRequest(
                "John Smith",
                "Toyota Camry 2020",
                "Oil change and brake inspection",
                LocalDate.of(2023, 12, 1),
                new BigDecimal("150.00")
        );
    }

    public static CreateJobRequest createJobRequest(String customerName, String carModel, String description, LocalDate date, BigDecimal income) {
        return new CreateJobRequest(customerName, carModel, description, date, income);
    }

    public static JobDto createJobDto() {
        return new JobDto(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
                "John Smith",
                "Toyota Camry 2020",
                "Oil change and brake inspection",
                LocalDate.of(2023, 12, 1),
                new BigDecimal("150.00"),
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
                List.of()
        );
    }

    public static JobSearchCriteria createJobSearchCriteria() {
        return new JobSearchCriteria(
                "John",
                "Toyota",
                "brake",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                new BigDecimal("100.00"),
                new BigDecimal("1000.00"),
                "brake pad",
                new BigDecimal("50.00"),
                new BigDecimal("500.00")
        );
    }

    public static JobSearchCriteria createJobSearchCriteria(String customerName, String carModel) {
        return new JobSearchCriteria(
                customerName,
                carModel,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static CreateJobRequest createInvalidJobRequest() {
        return new CreateJobRequest(
                "", // Invalid: empty customer name
                "", // Invalid: empty car model
                "", // Invalid: empty description
                null, // Invalid: null date
                null // Invalid: null income
        );
    }

    public static CreateJobRequest createJobRequestWithNegativeIncome() {
        return new CreateJobRequest(
                "Test Customer",
                "Test Car",
                "Test Description",
                LocalDate.now(),
                new BigDecimal("-100.00") // Invalid: negative income
        );
    }

    public static CreateJobRequest createJobRequestWithFutureDate() {
        return new CreateJobRequest(
                "Test Customer",
                "Test Car",
                "Test Description",
                LocalDate.now().plusDays(1), // Invalid: future date
                new BigDecimal("100.00")
        );
    }
} 