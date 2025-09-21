package com.mechtrack.service;

import com.mechtrack.AbstractMechtrackTest;
import com.mechtrack.exception.EntityNotFoundException;
import com.mechtrack.model.dto.CreateJobRequest;
import com.mechtrack.model.dto.JobDto;
import com.mechtrack.model.dto.JobSearchCriteria;
import com.mechtrack.model.enums.JobStatus;
import com.mechtrack.model.enums.JobType;
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
import java.util.UUID;

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

    @Test
    @DisplayName("Test create job")
    void testCreate() {
        var createJobRequest = createJobRequest();
        var job = underTest.createJob(createJobRequest);
        
        // Flush to ensure ID generation
        entityManager.flush();
        
        assertThat(job)
                .isNotNull()
                .hasFieldOrPropertyWithValue("customerName", createJobRequest.getCustomerName())
                .hasFieldOrPropertyWithValue("carModel", createJobRequest.getCarModel())
                .hasFieldOrPropertyWithValue("description", createJobRequest.getDescription())
                .hasFieldOrPropertyWithValue("date", createJobRequest.getDate())
                .hasFieldOrPropertyWithValue("income", createJobRequest.getIncome());
        
        assertNotNull(job.id(), "Job ID should not be null");
        assertNotNull(job.parts(), "Job parts should not be null");
        assertTrue(job.parts().isEmpty(), "Job parts should be empty");
    }

    @Test
    @DisplayName("Test get job by ID")
    void testGet() {
        var createJobRequest = createJobRequest();
        var job = underTest.createJob(createJobRequest);
        
        assertThat(underTest.getJobById(job.id()))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", job.id())
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
        
        assertThat(underTest.updateJob(job.id(), updateJobRequest))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", job.id())
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
        
        underTest.deleteJob(job.id());
        
        assertThatThrownBy(() -> underTest.getJobById(job.id()))
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
                .extracting(JobDto::id)
                .containsExactlyInAnyOrder(job1.id(), job2.id());
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
                .extracting(JobDto::id)
                .containsExactlyInAnyOrder(job1.id(), job2.id());
    }

    @Test
    @DisplayName("Test search jobs by customer name")
    @Transactional
    void testSearchByCustomerName() {
        var job1 = underTest.createJob(createJobRequest("John Smith", "Car 1", "Description 1", LocalDate.now(), new BigDecimal("100.00")));

        JobSearchCriteria criteria = new JobSearchCriteria();
        criteria.setCustomerName("Smith");
        List<JobDto> jobs = underTest.searchJobs(criteria, Pageable.unpaged()).getContent();
        
        assertThat(jobs)
                .hasSize(1)
                .extracting(JobDto::id)
                .containsExactly(job1.id());
    }

    @Test
    @DisplayName("Test search jobs by date range")
    void testSearchByDateRange() {
        var startDate = LocalDate.of(2023, 12, 1);
        var endDate = LocalDate.of(2023, 12, 31);
        var outsideDate = LocalDate.of(2024, 1, 1);
        
        var job1 = underTest.createJob(createJobRequest("Customer 1", "Car 1", "Description 1", startDate, new BigDecimal("100.00")));
        var job2 = underTest.createJob(createJobRequest("Customer 2", "Car 2", "Description 2", endDate, new BigDecimal("200.00")));
         underTest.createJob(createJobRequest("Customer 3", "Car 3", "Description 3", outsideDate, new BigDecimal("300.00")));
        
        JobSearchCriteria criteria = new JobSearchCriteria();
        criteria.setStartDate(startDate);
        criteria.setEndDate(endDate);
        List<JobDto> jobs = underTest.searchJobs(criteria, Pageable.unpaged()).getContent();
        
        assertThat(jobs)
                .hasSize(2)
                .extracting(JobDto::id)
                .containsExactlyInAnyOrder(job1.id(), job2.id());
    }

    @Test
    @DisplayName("Test search jobs with multiple criteria")
    void testSearchWithMultipleCriteria() {
        var job1 = underTest.createJob(createJobRequest("John Smith", "Toyota Camry", "Brake repair", LocalDate.of(2023, 12, 1), new BigDecimal("150.00")));
        underTest.createJob(createJobRequest("Mary Johnson", "Honda Civic", "Oil change", LocalDate.of(2023, 12, 5), new BigDecimal("75.00")));

        JobSearchCriteria criteria = new JobSearchCriteria();
        criteria.setCustomerName("John");
        criteria.setCarModel("Toyota");
        List<JobDto> jobs = underTest.searchJobs(criteria, Pageable.unpaged()).getContent();
        
        assertThat(jobs)
                .hasSize(1)
                .extracting(JobDto::id)
                .containsExactly(job1.id());
    }

    @Test
    @DisplayName("Test create job with default status and required type")
    void testCreateJobWithStatusAndType() {
        CreateJobRequest request = createJobRequestWithType("John Smith", "Toyota Camry", "Oil change", 
                LocalDate.of(2023, 12, 1), new BigDecimal("75.00"), JobType.OIL_CHANGE);
        
        JobDto createdJob = underTest.createJob(request);
        
        assertThat(createdJob).isNotNull();
        assertThat(createdJob.status()).isEqualTo(JobStatus.WAITING);
        assertThat(createdJob.type()).isEqualTo(JobType.OIL_CHANGE);
        assertThat(createdJob.customerName()).isEqualTo("John Smith");
    }

    @Test
    @DisplayName("Test update job status")
    void testUpdateJobStatus() {
        CreateJobRequest request = createJobRequestWithType("John Smith", "Toyota Camry", "Brake service", 
                LocalDate.of(2023, 12, 1), new BigDecimal("150.00"), JobType.BRAKE_SERVICE);
        JobDto createdJob = underTest.createJob(request);
        
        JobDto updatedJob = underTest.updateJobStatus(createdJob.id(), JobStatus.IN_PROGRESS);
        
        assertThat(updatedJob.status()).isEqualTo(JobStatus.IN_PROGRESS);
        assertThat(updatedJob.type()).isEqualTo(JobType.BRAKE_SERVICE);
        assertThat(updatedJob.id()).isEqualTo(createdJob.id());
    }

    @Test
    @DisplayName("Test update job status with non-existent job")
    void testUpdateJobStatusWithNonExistentJob() {
        UUID nonExistentId = UUID.randomUUID();
        
        assertThatThrownBy(() -> underTest.updateJobStatus(nonExistentId, JobStatus.DONE))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Job")
                .hasMessageContaining(nonExistentId.toString());
    }

    @Test
    @DisplayName("Test job status workflow")
    void testJobStatusWorkflow() {
        CreateJobRequest request = createJobRequestWithType("John Smith", "Honda Civic", "Engine tune-up", 
                LocalDate.of(2023, 12, 1), new BigDecimal("200.00"), JobType.ENGINE_TUNE_UP);
        JobDto job = underTest.createJob(request);
        
        assertThat(job.status()).isEqualTo(JobStatus.WAITING);
        
        job = underTest.updateJobStatus(job.id(), JobStatus.IN_PROGRESS);
        assertThat(job.status()).isEqualTo(JobStatus.IN_PROGRESS);
        
        job = underTest.updateJobStatus(job.id(), JobStatus.DONE);
        assertThat(job.status()).isEqualTo(JobStatus.DONE);
        
        job = underTest.updateJobStatus(job.id(), JobStatus.WAITING);
        assertThat(job.status()).isEqualTo(JobStatus.WAITING);
    }

    private CreateJobRequest createJobRequestWithType(String customerName, String carModel, String description, 
                                                     LocalDate date, BigDecimal income, JobType type) {
        CreateJobRequest request = new CreateJobRequest();
        request.setCustomerName(customerName);
        request.setCarModel(carModel);
        request.setDescription(description);
        request.setDate(date);
        request.setIncome(income);
        request.setType(type);
        return request;
    }
} 
