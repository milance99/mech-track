package com.mechtrack.api;

import com.mechtrack.model.dto.CreatePartRequest;
import com.mechtrack.model.dto.PartDto;
import com.mechtrack.service.FileStorageService;
import com.mechtrack.service.PartService;
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
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Parts", description = "Operations related to automotive parts management and inventory")
public class PartController {

    private final PartService partService;
    private final FileStorageService fileStorageService;

    @PostMapping("/jobs/{jobId}/parts")
    @Operation(
        summary = "Add a part to a job",
        description = "Associates a new automotive part with an existing repair job."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Part added to job successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PartDto.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<PartDto> addPartToJob(
            @Parameter(description = "Job ID", required = true) @PathVariable UUID jobId,
            @Parameter(description = "Part details", required = true) @Valid @RequestBody CreatePartRequest request) {
        
        PartDto createdPart = partService.addPartToJob(jobId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPart);
    }

    @PostMapping("/parts/{partId}/upload")
    @Operation(
        summary = "Upload invoice image for a part",
        description = "Uploads an invoice image for an existing part and updates the part with the file URL."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "File uploaded successfully and part updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PartDto.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Part not found"),
        @ApiResponse(responseCode = "400", description = "Invalid file or file upload failed")
    })
    public ResponseEntity<PartDto> uploadPartInvoice(
            @Parameter(description = "Part ID", required = true) @PathVariable String partId,
            @Parameter(description = "Invoice image file") @RequestParam("file") MultipartFile file) {
        
        PartDto updatedPart = partService.uploadPartInvoice(partId, file);
        return ResponseEntity.ok(updatedPart);
    }

    @GetMapping("/parts/{id}/invoice")
    @Operation(
        summary = "Download or preview part invoice",
        description = "Retrieves the invoice image for a specific part. Supports download or preview in browser."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice file retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Part or invoice file not found")
    })
    public ResponseEntity<Resource> getPartInvoice(
        @Parameter(description = "Unique identifier of the part", example = "650e8400-e29b-41d4-a716-446655440001")
        @PathVariable UUID id,
        @Parameter(description = "Whether to download the file or display inline", example = "false")
        @RequestParam(defaultValue = "false") boolean download) {
        
        PartDto part = partService.getPartById(id);
        
        if (part.getInvoiceImageUrl() == null || part.getInvoiceImageUrl().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            Resource file = fileStorageService.load(part.getInvoiceImageUrl());
            
            String contentDisposition = download ? "attachment" : "inline";
            String contentType = determineContentType(file.getFilename());
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition + "; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
                
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/parts")
    @Operation(
        summary = "Get all parts",
        description = "Retrieves a list of all automotive parts in the inventory across all jobs."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Parts retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PartDto.class)
            )
        )
    })
    public ResponseEntity<List<PartDto>> getAllParts() {
        List<PartDto> parts = partService.getAllParts();
        return ResponseEntity.ok(parts);
    }

    @GetMapping("/parts/paged")
    @Operation(
        summary = "Get all parts with pagination",
        description = "Retrieves a paginated list of all automotive parts in the inventory."
    )
    public ResponseEntity<Page<PartDto>> getAllPartsPaged(
        @PageableDefault(size = 20, sort = "purchaseDate") 
        @Parameter(description = "Pagination parameters (page, size, sort)") 
        Pageable pageable) {
        Page<PartDto> parts = partService.getAllParts(pageable);
        return ResponseEntity.ok(parts);
    }

    @GetMapping("/parts/{id}")
    @Operation(
        summary = "Get part by ID",
        description = "Retrieves a specific automotive part by its unique identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Part found and retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PartDto.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Part not found")
    })
    public ResponseEntity<PartDto> getPartById(
        @Parameter(description = "Unique identifier of the part", example = "650e8400-e29b-41d4-a716-446655440001")
        @PathVariable UUID id) {
        PartDto part = partService.getPartById(id);
        return ResponseEntity.ok(part);
    }

    @GetMapping("/parts/search")
    @Operation(
        summary = "Search parts",
        description = "Search parts by name. Simple and effective for workshop use."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<List<PartDto>> searchParts(
        @Parameter(description = "Part name (partial match)", example = "brake")
        @RequestParam(required = false) String name,
        
        @Parameter(description = "Job ID to filter parts", example = "550e8400-e29b-41d4-a716-446655440001")
        @RequestParam(required = false) UUID jobId) {
        
        List<PartDto> parts = partService.searchParts(name, jobId);
        return ResponseEntity.ok(parts);
    }

    @GetMapping("/jobs/{jobId}/parts")
    @Operation(
        summary = "Get parts for a specific job",
        description = "Retrieves all parts associated with a specific repair job."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Job not found")
    })
    public ResponseEntity<List<PartDto>> getPartsByJobId(
        @Parameter(description = "Unique identifier of the job", example = "550e8400-e29b-41d4-a716-446655440001")
        @PathVariable UUID jobId) {
        List<PartDto> parts = partService.getPartsByJobId(jobId);
        return ResponseEntity.ok(parts);
    }


    @PutMapping("/parts/{id}")
    @Operation(
        summary = "Update an existing part",
        description = "Updates the details of an existing automotive part."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Part updated successfully"),
        @ApiResponse(responseCode = "404", description = "Part not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<PartDto> updatePart(
        @Parameter(description = "Unique identifier of the part to update", example = "650e8400-e29b-41d4-a716-446655440001")
        @PathVariable UUID id,
        @Valid @RequestBody CreatePartRequest request) {
        PartDto updatedPart = partService.updatePart(id, request);
        return ResponseEntity.ok(updatedPart);
    }

    @DeleteMapping("/parts/{id}")
    @Operation(
        summary = "Delete a part",
        description = "Removes an automotive part from the inventory and associated job."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Part deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Part not found")
    })
    public ResponseEntity<Void> deletePart(
        @Parameter(description = "Unique identifier of the part to delete", example = "650e8400-e29b-41d4-a716-446655440001")
        @PathVariable UUID id) {
        partService.deletePart(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Determine the content type based on file extension
     */
    private String determineContentType(String filename) {
        if (filename == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        
        String lowerCaseFilename = filename.toLowerCase();
        
        if (lowerCaseFilename.endsWith(".png")) {
            return "image/png";
        } else if (lowerCaseFilename.endsWith(".jpg") || lowerCaseFilename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerCaseFilename.endsWith(".pdf")) {
            return "application/pdf";
        } else if (lowerCaseFilename.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerCaseFilename.endsWith(".webp")) {
            return "image/webp";
        } else {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
} 
