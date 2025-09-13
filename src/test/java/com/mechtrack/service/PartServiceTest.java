package com.mechtrack.service;

import com.mechtrack.AbstractMechtrackTest;
import com.mechtrack.model.dto.PartDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.mechtrack.provider.JobTestDataProvider.createJobRequest;
import static com.mechtrack.provider.PartTestDataProvider.createPartRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PartServiceTest extends AbstractMechtrackTest {

    @Autowired
    private PartService underTest;
    
    @Autowired
    private JobService jobService;

    @Test
    @DisplayName("Test add part to job")
    void testAddPartToJob() {
        var job = jobService.createJob(createJobRequest());
        var createPartRequest = createPartRequest();
        
        var part = underTest.addPartToJob(job.getId(), createPartRequest);
        
        assertThat(part)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", createPartRequest.getName())
                .hasFieldOrPropertyWithValue("cost", createPartRequest.getCost())
                .hasFieldOrPropertyWithValue("invoiceImageUrl", createPartRequest.getInvoiceImageUrl())
                .hasFieldOrPropertyWithValue("purchaseDate", createPartRequest.getPurchaseDate())
                .hasFieldOrPropertyWithValue("jobId", job.getId());
        
        assertThat(part.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test get part by ID")
    void testGet() {
        var job = jobService.createJob(createJobRequest());
        var createPartRequest = createPartRequest();
        var part = underTest.addPartToJob(job.getId(), createPartRequest);
        
        assertThat(underTest.getPartById(part.getId()))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", part.getId())
                .hasFieldOrPropertyWithValue("name", createPartRequest.getName())
                .hasFieldOrPropertyWithValue("cost", createPartRequest.getCost())
                .hasFieldOrPropertyWithValue("invoiceImageUrl", createPartRequest.getInvoiceImageUrl())
                .hasFieldOrPropertyWithValue("purchaseDate", createPartRequest.getPurchaseDate())
                .hasFieldOrPropertyWithValue("jobId", job.getId());
    }

    @Test
    @DisplayName("Test update part")
    void testUpdate() {
        var job = jobService.createJob(createJobRequest());
        var createPartRequest = createPartRequest();
        var part = underTest.addPartToJob(job.getId(), createPartRequest);
        
        assertThat(part)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", createPartRequest.getName())
                .hasFieldOrPropertyWithValue("cost", createPartRequest.getCost());

        var updatePartRequest = createPartRequest(
                "Updated Brake Pads", 
                new BigDecimal("150.00"), 
                "https://example.com/updated-invoice.jpg", 
                LocalDate.of(2023, 12, 1)
        );
        
        assertThat(underTest.updatePart(part.getId(), updatePartRequest))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", part.getId())
                .hasFieldOrPropertyWithValue("name", updatePartRequest.getName())
                .hasFieldOrPropertyWithValue("cost", updatePartRequest.getCost())
                .hasFieldOrPropertyWithValue("invoiceImageUrl", updatePartRequest.getInvoiceImageUrl())
                .hasFieldOrPropertyWithValue("purchaseDate", updatePartRequest.getPurchaseDate())
                .hasFieldOrPropertyWithValue("jobId", job.getId());
    }

    @Test
    @DisplayName("Test delete part")
    void testDelete() {
        var job = jobService.createJob(createJobRequest());
        var createPartRequest = createPartRequest();
        var part = underTest.addPartToJob(job.getId(), createPartRequest);
        
        underTest.deletePart(part.getId());
        
        assertThatThrownBy(() -> underTest.getPartById(part.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Part not found");
    }

    @Test
    @DisplayName("Test get all parts")
    void testGetAll() {
        var job = jobService.createJob(createJobRequest());
        var part1 = underTest.addPartToJob(job.getId(), createPartRequest("Part 1", new BigDecimal("50.00"), "url1", LocalDate.now()));
        var part2 = underTest.addPartToJob(job.getId(), createPartRequest("Part 2", new BigDecimal("75.00"), "url2", LocalDate.now()));
        
        List<PartDto> parts = underTest.getAllParts();
        
        assertThat(parts)
                .hasSize(2)
                .extracting(PartDto::getId)
                .containsExactlyInAnyOrder(part1.getId(), part2.getId());
    }

    @Test
    @DisplayName("Test get all parts with pagination")
    void testGetAllPaged() {
        var job = jobService.createJob(createJobRequest());
        var part1 = underTest.addPartToJob(job.getId(), createPartRequest("Part 1", new BigDecimal("50.00"), "url1", LocalDate.now()));
        var part2 = underTest.addPartToJob(job.getId(), createPartRequest("Part 2", new BigDecimal("75.00"), "url2", LocalDate.now()));
        
        Page<PartDto> partsPage = underTest.getAllParts(Pageable.unpaged());
        
        assertThat(partsPage)
                .isNotNull();
        assertThat(partsPage.getContent())
                .hasSize(2)
                .extracting(PartDto::getId)
                .containsExactlyInAnyOrder(part1.getId(), part2.getId());
    }

    @Test
    @DisplayName("Test get parts by job ID")
    void testGetPartsByJobId() {
        var job1 = jobService.createJob(createJobRequest("Customer 1", "Car 1", "Desc 1", LocalDate.now(), new BigDecimal("100.00")));
        var job2 = jobService.createJob(createJobRequest("Customer 2", "Car 2", "Desc 2", LocalDate.now(), new BigDecimal("200.00")));
        
        var part1 = underTest.addPartToJob(job1.getId(), createPartRequest("Part 1", new BigDecimal("50.00"), "url1", LocalDate.now()));
        var part2 = underTest.addPartToJob(job1.getId(), createPartRequest("Part 2", new BigDecimal("75.00"), "url2", LocalDate.now()));
        var part3 = underTest.addPartToJob(job2.getId(), createPartRequest("Part 3", new BigDecimal("100.00"), "url3", LocalDate.now()));
        
        List<PartDto> job1Parts = underTest.getPartsByJobId(job1.getId());
        
        assertThat(job1Parts)
                .hasSize(2)
                .extracting(PartDto::getId)
                .containsExactlyInAnyOrder(part1.getId(), part2.getId());
    }

    @Test
    @DisplayName("Test search parts by name")
    void testSearchByName() {
        var job = jobService.createJob(createJobRequest());
        var part1 = underTest.addPartToJob(job.getId(), createPartRequest("Brake Pads", new BigDecimal("50.00"), "url1", LocalDate.now()));
        var part2 = underTest.addPartToJob(job.getId(), createPartRequest("Oil Filter", new BigDecimal("25.00"), "url2", LocalDate.now()));
        
        List<PartDto> parts = underTest.searchParts("Brake", null);
        
        assertThat(parts)
                .hasSize(1)
                .extracting(PartDto::getId)
                .containsExactly(part1.getId());
    }

    @Test
    @DisplayName("Test search parts with criteria")
    void testSearchWithCriteria() {
        var job = jobService.createJob(createJobRequest("John Smith", "Toyota Camry", "Brake repair", LocalDate.now(), new BigDecimal("200.00")));
        var part1 = underTest.addPartToJob(job.getId(), createPartRequest("Brake Pads", new BigDecimal("80.00"), "url1", LocalDate.of(2023, 12, 1)));
        var part2 = underTest.addPartToJob(job.getId(), createPartRequest("Oil Filter", new BigDecimal("25.00"), "url2", LocalDate.of(2023, 12, 2)));
        
        List<PartDto> parts = underTest.searchParts("Brake", job.getId());
        
        assertThat(parts)
                .hasSize(1)
                .extracting(PartDto::getId)
                .containsExactly(part1.getId());
    }
} 
