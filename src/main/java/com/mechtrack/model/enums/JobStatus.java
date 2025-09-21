package com.mechtrack.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Job status enumeration")
public enum JobStatus {
    
    @Schema(description = "Job is scheduled but not started yet")
    WAITING,
    
    @Schema(description = "Job is currently being worked on")
    IN_PROGRESS,
    
    @Schema(description = "Job has been completed successfully")
    DONE
}
