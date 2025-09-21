package com.mechtrack.api;

import com.mechtrack.model.dto.CreateJobRequest;
import com.mechtrack.model.dto.JobDto;
import com.mechtrack.model.dto.JobSearchCriteria;
import com.mechtrack.model.enums.JobStatus;
import com.mechtrack.model.enums.JobType;
import com.mechtrack.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Jobs", description = "Operations related to automotive repair jobs management")
public class JobController {

    private final JobService jobService;

    @PostMapping
    @Operation(
        summary = "Create a new job",
        description = "Creates a new automotive repair job with the provided details. The job will be assigned a unique UUID automatically."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Job created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = JobDto.class),
                examples = @ExampleObject(
                    name = "Created Job",
                    value = """
                    {
                        "id": "550e8400-e29b-41d4-a716-446655440001",
                        "customerName": "John Smith",
                        "carModel": "Toyota Camry 2020",
                        "description": "Oil change and brake inspection",
                        "date": "2023-12-01",
                        "income": 150.00,
                        "parts": []
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobDto> createJob(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Job creation details",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CreateJobRequest.class),
                examples = @ExampleObject(
                    name = "Job Creation Request",
                    value = """
                    {
                        "customerName": "John Smith",
                        "carModel": "Toyota Camry 2020",
                        "description": "Oil change and brake inspection",
                        "date": "2023-12-01",
                        "income": 150.00
                    }
                    """
                )
            )
        )
        @Valid @RequestBody CreateJobRequest request) {
        JobDto createdJob = jobService.createJob(request);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Get all jobs",
        description = "Retrieves a list of all automotive repair jobs in the system. Supports pagination for large datasets."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Jobs retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = JobDto.class)
            )
        )
    })
    public ResponseEntity<List<JobDto>> getAllJobs() {
        List<JobDto> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/paged")
    @Operation(
        summary = "Get all jobs with pagination",
        description = "Retrieves a paginated list of all automotive repair jobs in the system."
    )
    public ResponseEntity<Page<JobDto>> getAllJobsPaged(
        @PageableDefault(size = 20, sort = "date") 
        @Parameter(description = "Pagination parameters (page, size, sort)") 
        Pageable pageable) {
        Page<JobDto> jobs = jobService.getAllJobs(pageable);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get job by ID",
        description = "Retrieves a specific job by its unique identifier, including all associated parts."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Job found and retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = JobDto.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Job not found")
    })
    public ResponseEntity<JobDto> getJobById(
        @Parameter(description = "Unique identifier of the job", example = "550e8400-e29b-41d4-a716-446655440001")
        @PathVariable UUID id) {
        JobDto job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search jobs with comprehensive filters",
        description = "Search jobs by any combination of customer name, car model, description, date range, income range, status, type, and part information. All parameters are optional and can be combined for powerful filtering."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<List<JobDto>> searchJobs(
        @Parameter(description = "Customer name (partial match, case-insensitive)", example = "John")
        @RequestParam(required = false) String customerName,
        
        @Parameter(description = "Car model (partial match, case-insensitive)", example = "Toyota")
        @RequestParam(required = false) String carModel,
        
        @Parameter(description = "Job description (partial match, case-insensitive)", example = "brake")
        @RequestParam(required = false) String description,
        
        @Parameter(description = "Start date (YYYY-MM-DD)", example = "2023-01-01")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        
        @Parameter(description = "End date (YYYY-MM-DD)", example = "2023-12-31")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        
        @Parameter(description = "Minimum income", example = "100.00")
        @RequestParam(required = false) BigDecimal minIncome,
        
        @Parameter(description = "Maximum income", example = "1000.00")
        @RequestParam(required = false) BigDecimal maxIncome,
        
        @Parameter(description = "Job status", example = "IN_PROGRESS")
        @RequestParam(required = false) JobStatus status,
        
        @Parameter(description = "Multiple job statuses (comma-separated)", example = "WAITING,IN_PROGRESS")
        @RequestParam(required = false) List<JobStatus> statuses,
        
        @Parameter(description = "Job type", example = "OIL_CHANGE")
        @RequestParam(required = false) JobType type,
        
        @Parameter(description = "Multiple job types (comma-separated)", example = "OIL_CHANGE,BRAKE_SERVICE")
        @RequestParam(required = false) List<JobType> types,
        
        @Parameter(description = "Part name (partial match, case-insensitive)", example = "brake pad")
        @RequestParam(required = false) String partName,
        
        @Parameter(description = "Minimum part cost", example = "50.00")
        @RequestParam(required = false) BigDecimal minPartCost,
        
        @Parameter(description = "Maximum part cost", example = "500.00")
        @RequestParam(required = false) BigDecimal maxPartCost) {
        
        JobSearchCriteria criteria = buildSearchCriteria(customerName, carModel, description, startDate, endDate, 
                minIncome, maxIncome, status, statuses, type, types, partName, minPartCost, maxPartCost);
        
        List<JobDto> jobs = jobService.searchJobs(criteria, Pageable.unpaged()).getContent();
        return ResponseEntity.ok(jobs);
    }


    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing job",
        description = "Updates all fields of an existing job. The job ID cannot be changed."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Job updated successfully"),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<JobDto> updateJob(
        @Parameter(description = "Unique identifier of the job to update")
        @PathVariable UUID id, 
        @Valid @RequestBody CreateJobRequest request) {
        JobDto updatedJob = jobService.updateJob(id, request);
        return ResponseEntity.ok(updatedJob);
    }

    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Update job status",
        description = "Updates only the status of an existing job. Useful for workflow management."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Job status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "400", description = "Invalid status value")
    })
    public ResponseEntity<JobDto> updateJobStatus(
        @Parameter(description = "Unique identifier of the job to update")
        @PathVariable UUID id,
        @Parameter(description = "New status for the job", example = "IN_PROGRESS")
        @RequestParam JobStatus status) {
        JobDto updatedJob = jobService.updateJobStatus(id, status);
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a job",
        description = "Permanently deletes a job and all its associated parts from the system. This action cannot be undone."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Job deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Job not found")
    })
    public ResponseEntity<Void> deleteJob(
        @Parameter(description = "Unique identifier of the job to delete")
        @PathVariable UUID id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    private JobSearchCriteria buildSearchCriteria(String customerName, String carModel, String description,
                                                LocalDate startDate, LocalDate endDate, BigDecimal minIncome, 
                                                BigDecimal maxIncome, JobStatus status, List<JobStatus> statuses,
                                                JobType type, List<JobType> types, String partName, 
                                                BigDecimal minPartCost, BigDecimal maxPartCost) {
        JobSearchCriteria criteria = new JobSearchCriteria();
        criteria.setCustomerName(customerName);
        criteria.setCarModel(carModel);
        criteria.setDescription(description);
        criteria.setStartDate(startDate);
        criteria.setEndDate(endDate);
        criteria.setMinIncome(minIncome);
        criteria.setMaxIncome(maxIncome);
        criteria.setStatus(status);
        criteria.setStatuses(statuses);
        criteria.setType(type);
        criteria.setTypes(types);
        criteria.setPartName(partName);
        criteria.setMinPartCost(minPartCost);
        criteria.setMaxPartCost(maxPartCost);
        return criteria;
    }
} 
