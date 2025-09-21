package com.mechtrack.service;

import com.mechtrack.AbstractMechtrackTest;
import com.mechtrack.model.dto.PartDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

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
        
        var part = underTest.addPartToJob(job.id(), createPartRequest, null);

        assertThat(part)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", createPartRequest.getName())
                .hasFieldOrPropertyWithValue("cost", createPartRequest.getCost())
                .hasFieldOrPropertyWithValue("purchaseDate", createPartRequest.getPurchaseDate())
                .hasFieldOrPropertyWithValue("jobId", job.id());
        
        assertThat(part.id()).isNotNull();
    }

    @Test
    @DisplayName("Test get part by ID")
    void testGet() {
        var job = jobService.createJob(createJobRequest());
        var createPartRequest = createPartRequest();
        var part = underTest.addPartToJob(job.id(), createPartRequest, null);

        assertThat(underTest.getPartById(part.id()))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", part.id())
                .hasFieldOrPropertyWithValue("name", createPartRequest.getName())
                .hasFieldOrPropertyWithValue("cost", createPartRequest.getCost())
                .hasFieldOrPropertyWithValue("purchaseDate", createPartRequest.getPurchaseDate())
                .hasFieldOrPropertyWithValue("jobId", job.id());
    }

    @Test
    @DisplayName("Test update part")
    void testUpdate() {
        var job = jobService.createJob(createJobRequest());
        var createPartRequest = createPartRequest();
        var part = underTest.addPartToJob(job.id(), createPartRequest, null);

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
        
        assertThat(underTest.updatePart(part.id(), updatePartRequest))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", part.id())
                .hasFieldOrPropertyWithValue("name", updatePartRequest.getName())
                .hasFieldOrPropertyWithValue("cost", updatePartRequest.getCost())
                .hasFieldOrPropertyWithValue("invoiceImageUrl", updatePartRequest.getInvoiceImageUrl())
                .hasFieldOrPropertyWithValue("purchaseDate", updatePartRequest.getPurchaseDate())
                .hasFieldOrPropertyWithValue("jobId", job.id());
    }

    @Test
    @DisplayName("Test delete part")
    void testDelete() {
        var job = jobService.createJob(createJobRequest());
        var createPartRequest = createPartRequest();
        var part = underTest.addPartToJob(job.id(), createPartRequest, null);

        underTest.deletePart(part.id());
        
        assertThatThrownBy(() -> underTest.getPartById(part.id()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Part not found");
    }

    @Test
    @DisplayName("Test get all parts")
    void testGetAll() {
        var job = jobService.createJob(createJobRequest());
        var part1 = underTest.addPartToJob(job.id(), createPartRequest(), null);
        var part2 = underTest.addPartToJob(job.id(), createPartRequest("Part 2", new BigDecimal("75.00"), "url2", LocalDate.now()), null);

        List<PartDto> parts = underTest.getAllParts();
        
        assertThat(parts)
                .hasSize(2)
                .extracting(PartDto::id)
                .containsExactlyInAnyOrder(part1.id(), part2.id());
    }

    @Test
    @DisplayName("Test get all parts with pagination")
    void testGetAllPaged() {
        var job = jobService.createJob(createJobRequest());
        var part1 = underTest.addPartToJob(job.id(), createPartRequest(), null);
        var part2 = underTest.addPartToJob(job.id(), createPartRequest("Part 2", new BigDecimal("75.00"), "url2", LocalDate.now()), null);

        Page<PartDto> partsPage = underTest.getAllParts(Pageable.unpaged());
        
        assertThat(partsPage)
                .isNotNull();
        assertThat(partsPage.getContent())
                .hasSize(2)
                .extracting(PartDto::id)
                .containsExactlyInAnyOrder(part1.id(), part2.id());
    }

    @Test
    @DisplayName("Test get parts by job ID")
    void testGetPartsByJobId() {
        var job1 = jobService.createJob(createJobRequest("Customer 1", "Car 1", "Desc 1", LocalDate.now(), new BigDecimal("100.00")));
        
        var part1 = underTest.addPartToJob(job1.id(), createPartRequest(), null);
        var part2 = underTest.addPartToJob(job1.id(), createPartRequest("Part 2", new BigDecimal("75.00"), "url2", LocalDate.now()), null);


        List<PartDto> job1Parts = underTest.getPartsByJobId(job1.id());
        
        assertThat(job1Parts)
                .hasSize(2)
                .extracting(PartDto::id)
                .containsExactlyInAnyOrder(part1.id(), part2.id());
    }

    @Test
    @DisplayName("Test search parts by name")
    void testSearchByName() {
        var job = jobService.createJob(createJobRequest());
        var part1 = underTest.addPartToJob(job.id(), createPartRequest(), null);
        underTest.addPartToJob(job.id(), createPartRequest("Oil Filter", new BigDecimal("25.00"), "url2", LocalDate.now()), null);

        List<PartDto> parts = underTest.searchParts("Brake", null);
        
        assertThat(parts)
                .hasSize(1)
                .extracting(PartDto::id)
                .containsExactly(part1.id());
    }

    @Test
    @DisplayName("Test search parts with criteria")
    void testSearchWithCriteria() {
        var job = jobService.createJob(createJobRequest("John Smith", "Toyota Camry", "Brake repair", LocalDate.now(), new BigDecimal("200.00")));
        var part1 = underTest.addPartToJob(job.id(), createPartRequest(), null);
        underTest.addPartToJob(job.id(), createPartRequest("Oil Filter", new BigDecimal("25.00"), "url2", LocalDate.of(2023, 12, 2)), null);

        List<PartDto> parts = underTest.searchParts("Brake", job.id());
        
        assertThat(parts)
                .hasSize(1)
                .extracting(PartDto::id)
                .containsExactly(part1.id());
    }

    @Test
    @DisplayName("Test add part to job with file upload")
    void testAddPartToJobWithFileUpload() {
        var job = jobService.createJob(createJobRequest());
        var createPartRequest = createPartRequest();

        MockMultipartFile mockFile = new MockMultipartFile(
                "invoice",
                "test-invoice.pdf",
                "application/pdf",
                "Test invoice content".getBytes()
        );

        var part = underTest.addPartToJob(job.id(), createPartRequest, mockFile);

        assertThat(part)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", createPartRequest.getName())
                .hasFieldOrPropertyWithValue("cost", createPartRequest.getCost())
                .hasFieldOrPropertyWithValue("purchaseDate", createPartRequest.getPurchaseDate())
                .hasFieldOrPropertyWithValue("jobId", job.id());

        assertThat(part.invoiceImageUrl()).isNotNull();
        assertThat(part.invoiceImageUrl()).contains("invoices/");
    }
}
