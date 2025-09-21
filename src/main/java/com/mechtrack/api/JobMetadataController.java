package com.mechtrack.api;

import com.mechtrack.model.enums.JobStatus;
import com.mechtrack.model.enums.JobType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/jobs/metadata")
@Tag(name = "Job Metadata", description = "Endpoints for retrieving job metadata like types and statuses")
public class JobMetadataController {

    @GetMapping("/types")
    @Operation(
        summary = "Get all job types",
        description = "Retrieves all available job types for automotive workshop services"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved job types",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = JobType.class))
            )
        )
    })
    public ResponseEntity<List<JobType>> getJobTypes() {
        return ResponseEntity.ok(Arrays.asList(JobType.values()));
    }

    @GetMapping("/statuses")
    @Operation(
        summary = "Get all job statuses",
        description = "Retrieves all available job statuses"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved job statuses",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = JobStatus.class))
            )
        )
    })
    public ResponseEntity<List<JobStatus>> getJobStatuses() {
        return ResponseEntity.ok(Arrays.asList(JobStatus.values()));
    }
}
