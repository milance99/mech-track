package com.mechtrack.provider;

import com.mechtrack.model.dto.CreatePartRequest;
import com.mechtrack.model.dto.PartDto;
import com.mechtrack.model.dto.PartSearchCriteria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Test data provider for Part-related DTOs
 */
public class PartTestDataProvider {

    public static CreatePartRequest createPartRequest() {
        return new CreatePartRequest(
                "Brake Pads",
                new BigDecimal("120.00"),
                "https://example.com/invoice.jpg",
                LocalDate.of(2023, 11, 30)
        );
    }

    public static CreatePartRequest createPartRequest(String name, BigDecimal cost, String invoiceUrl, LocalDate purchaseDate) {
        return new CreatePartRequest(name, cost, invoiceUrl, purchaseDate);
    }

    public static PartDto createPartDto() {
        return new PartDto(
                UUID.fromString("650e8400-e29b-41d4-a716-446655440001"),
                "Brake Pads",
                new BigDecimal("120.00"),
                "https://example.com/invoice.jpg",
                LocalDate.of(2023, 11, 30),
                UUID.fromString("550e8400-e29b-41d4-a716-446655440001")
        );
    }

    public static PartDto createPartDto(UUID id, String name, UUID jobId) {
        return new PartDto(
                id,
                name,
                new BigDecimal("50.00"),
                "https://example.com/invoice.jpg",
                LocalDate.now(),
                jobId
        );
    }

    public static PartSearchCriteria createPartSearchCriteria() {
        return new PartSearchCriteria(
                "brake",
                UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
                new BigDecimal("10.00"),
                new BigDecimal("200.00"),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                "John Smith",
                "Toyota"
        );
    }

    public static PartSearchCriteria createPartSearchCriteria(String partName, UUID jobId) {
        return new PartSearchCriteria(
                partName,
                jobId,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static CreatePartRequest createInvalidPartRequest() {
        return new CreatePartRequest(
                "", // Invalid: empty name
                null, // Invalid: null cost
                null, // Null invoice URL (can be valid)
                null // Invalid: null purchase date
        );
    }

    public static CreatePartRequest createPartRequestWithNegativeCost() {
        return new CreatePartRequest(
                "Test Part",
                new BigDecimal("-50.00"), // Invalid: negative cost
                "https://example.com/invoice.jpg",
                LocalDate.now()
        );
    }

    public static CreatePartRequest createPartRequestWithFutureDate() {
        return new CreatePartRequest(
                "Test Part",
                new BigDecimal("50.00"),
                "https://example.com/invoice.jpg",
                LocalDate.now().plusDays(1) // Invalid: future purchase date
        );
    }

    public static CreatePartRequest createPartRequestWithInvalidUrl() {
        return new CreatePartRequest(
                "Test Part",
                new BigDecimal("50.00"),
                "invalid-url", // Invalid: malformed URL
                LocalDate.now()
        );
    }
} 