package com.mechtrack.service;

import com.mechtrack.AbstractMechtrackTest;
import com.mechtrack.model.dto.JobDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.mechtrack.provider.JobTestDataProvider.createJobRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
class JobServiceTest extends AbstractMechtrackTest {

    @Autowired
    private JobService underTest;
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private com.mechtrack.repository.JobRepository jobRepository;

    @Test
    @DisplayName("Test create job")
    void testCreate() {
        var createJobRequest = createJobRequest();
        var job = underTest.createJob(createJobRequest);
        
        // Flush to ensure ID generation
        entityManager.flush();
        
        // Debug: Check if any entities were actually saved
        var allJobs = jobRepository.findAll();
        System.out.println("Number of jobs in database: " + allJobs.size());
        if (!allJobs.isEmpty()) {
            var savedJob = allJobs.get(0);
            System.out.println("Saved job ID: " + savedJob.getId());
            System.out.println("Saved job customer: " + savedJob.getCustomerName());
        }
        
        // Debug: Print the DTO we got back
        System.out.println("DTO ID: " + job.getId());
        System.out.println("DTO customer: " + job.getCustomerName());
        System.out.println("DTO ID == null: " + (job.getId() == null));
        System.out.println("DTO ID class: " + (job.getId() != null ? job.getId().getClass() : "null"));
        
        assertThat(job)
                .isNotNull()
                .hasFieldOrPropertyWithValue("customerName", createJobRequest.getCustomerName())
                .hasFieldOrPropertyWithValue("carModel", createJobRequest.getCarModel())
                .hasFieldOrPropertyWithValue("description", createJobRequest.getDescription())
                .hasFieldOrPropertyWithValue("date", createJobRequest.getDate())
                .hasFieldOrPropertyWithValue("income", createJobRequest.getIncome());
        
        // Add specific debugging for the ID assertion
        System.out.println("About to check ID assertion...");
        System.out.println("job.getId() before assertion: " + job.getId());
        
        // Try JUnit assertions instead of AssertJ
        assertNotNull(job.getId(), "Job ID should not be null");
        assertNotNull(job.getParts(), "Job parts should not be null");
        assertTrue(job.getParts().isEmpty(), "Job parts should be empty");
    }

    @Test
    @DisplayName("Test get job by ID")
    void testGet() {
        var createJobRequest = createJobRequest();
        var job = underTest.createJob(createJobRequest);
        
        assertThat(underTest.getJobById(job.getId()))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", job.getId())
                .hasFieldOrPropertyWithValue("customerName", createJobRequest.getCustomerName())
                .hasFieldOrPropertyWithValue("carModel", createJobRequest.getCarModel())
                .hasFieldOrPropertyWithValue("description", createJobRequest.getDescription())
                .hasFieldOrPropertyWithValue("date", createJobRequest.getDate())
                .hasFieldOrPropertyWithValue("income", createJobRequest.getIncome());
    }

    @Test
    @DisplayName("Test update job")
    void testUpdate() {
        var createJobRequest = createJobRequest();
        var job = underTest.createJob(createJobRequest);
        
        assertThat(job)
                .isNotNull()
                .hasFieldOrPropertyWithValue("customerName", createJobRequest.getCustomerName())
                .hasFieldOrPropertyWithValue("carModel", createJobRequest.getCarModel());

        var updateJobRequest = createJobRequest(
                "Mary Johnson", 
                "Honda Civic 2021", 
                "Transmission service", 
                LocalDate.of(2023, 12, 15), 
                new BigDecimal("300.00")
        );
        
        assertThat(underTest.updateJob(job.getId(), updateJobRequest))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", job.getId())
                .hasFieldOrPropertyWithValue("customerName", updateJobRequest.getCustomerName())
                .hasFieldOrPropertyWithValue("carModel", updateJobRequest.getCarModel())
                .hasFieldOrPropertyWithValue("description", updateJobRequest.getDescription())
                .hasFieldOrPropertyWithValue("date", updateJobRequest.getDate())
                .hasFieldOrPropertyWithValue("income", updateJobRequest.getIncome());
    }

    @Test
    @DisplayName("Test delete job")
    void testDelete() {
        var createJobRequest = createJobRequest();
        var job = underTest.createJob(createJobRequest);
        
        underTest.deleteJob(job.getId());
        
        assertThatThrownBy(() -> underTest.getJobById(job.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Job not found");
    }

    @Test
    @DisplayName("Test get all jobs")
    void testGetAll() {
        var job1 = underTest.createJob(createJobRequest("Customer 1", "Car 1", "Description 1", LocalDate.now(), new BigDecimal("100.00")));
        var job2 = underTest.createJob(createJobRequest("Customer 2", "Car 2", "Description 2", LocalDate.now(), new BigDecimal("200.00")));
        
        List<JobDto> jobs = underTest.getAllJobs();
        
        assertThat(jobs)
                .hasSize(2)
                .extracting(JobDto::getId)
                .containsExactlyInAnyOrder(job1.getId(), job2.getId());
    }

    @Test
    @DisplayName("Test get all jobs with pagination")
    void testGetAllPaged() {
        var job1 = underTest.createJob(createJobRequest("Customer 1", "Car 1", "Description 1", LocalDate.now(), new BigDecimal("100.00")));
        var job2 = underTest.createJob(createJobRequest("Customer 2", "Car 2", "Description 2", LocalDate.now(), new BigDecimal("200.00")));
        
        Page<JobDto> jobsPage = underTest.getAllJobs(Pageable.unpaged());
        
        assertThat(jobsPage)
                .isNotNull();
        assertThat(jobsPage.getContent())
                .hasSize(2)
                .extracting(JobDto::getId)
                .containsExactlyInAnyOrder(job1.getId(), job2.getId());
    }

    @Test
    @DisplayName("Test search jobs by customer name")
    @Transactional
    void testSearchByCustomerName() {
        var job1 = underTest.createJob(createJobRequest("John Smith", "Car 1", "Description 1", LocalDate.now(), new BigDecimal("100.00")));
        var job2 = underTest.createJob(createJobRequest("Mary Wilson", "Car 2", "Description 2", LocalDate.now(), new BigDecimal("200.00")));
        
        List<JobDto> jobs = underTest.searchJobs("Smith", null, null, null, null, null);
        
        assertThat(jobs)
                .hasSize(1)
                .extracting(JobDto::getId)
                .containsExactly(job1.getId());
    }

    @Test
    @DisplayName("Test search jobs by date range")
    void testSearchByDateRange() {
        var startDate = LocalDate.of(2023, 12, 1);
        var endDate = LocalDate.of(2023, 12, 31);
        var outsideDate = LocalDate.of(2024, 1, 1);
        
        var job1 = underTest.createJob(createJobRequest("Customer 1", "Car 1", "Description 1", startDate, new BigDecimal("100.00")));
        var job2 = underTest.createJob(createJobRequest("Customer 2", "Car 2", "Description 2", endDate, new BigDecimal("200.00")));
        var job3 = underTest.createJob(createJobRequest("Customer 3", "Car 3", "Description 3", outsideDate, new BigDecimal("300.00")));
        
        List<JobDto> jobs = underTest.searchJobs(null, null, startDate, endDate, null, null);
        
        assertThat(jobs)
                .hasSize(2)
                .extracting(JobDto::getId)
                .containsExactlyInAnyOrder(job1.getId(), job2.getId());
    }

    @Test
    @DisplayName("Test search jobs with multiple criteria")
    void testSearchWithMultipleCriteria() {
        var job1 = underTest.createJob(createJobRequest("John Smith", "Toyota Camry", "Brake repair", LocalDate.of(2023, 12, 1), new BigDecimal("150.00")));
        var job2 = underTest.createJob(createJobRequest("Mary Johnson", "Honda Civic", "Oil change", LocalDate.of(2023, 12, 5), new BigDecimal("75.00")));
        
        List<JobDto> jobs = underTest.searchJobs("John", "Toyota", null, null, null, null);
        
        assertThat(jobs)
                .hasSize(1)
                .extracting(JobDto::getId)
                .containsExactly(job1.getId());
    }
} 
