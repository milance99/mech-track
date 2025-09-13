package com.mechtrack.api;

import com.mechtrack.model.dto.CreateJobRequest;
import com.mechtrack.model.dto.JobDto;
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
        summary = "Search jobs",
        description = "Search jobs by customer name, car model, or date range. All parameters are optional."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<List<JobDto>> searchJobs(
        @Parameter(description = "Customer name (partial match)", example = "John")
        @RequestParam(required = false) String customer,
        
        @Parameter(description = "Car model (partial match)", example = "Toyota")
        @RequestParam(required = false) String car,
        
        @Parameter(description = "Start date (YYYY-MM-DD)", example = "2023-01-01")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        
        @Parameter(description = "End date (YYYY-MM-DD)", example = "2023-12-31")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
        
        @Parameter(description = "Minimum income", example = "100.00")
        @RequestParam(required = false) BigDecimal minIncome,
        
        @Parameter(description = "Maximum income", example = "1000.00")
        @RequestParam(required = false) BigDecimal maxIncome) {
        
        List<JobDto> jobs = jobService.searchJobs(customer, car, from, to, minIncome, maxIncome);
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
} 
