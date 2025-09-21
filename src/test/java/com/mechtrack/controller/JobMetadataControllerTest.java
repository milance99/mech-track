package com.mechtrack.controller;

import com.mechtrack.AbstractMechtrackMvcTest;
import com.mechtrack.model.enums.JobStatus;
import com.mechtrack.model.enums.JobType;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class JobMetadataControllerTest extends AbstractMechtrackMvcTest {

    @Test
    void getJobTypes_ShouldReturnAllJobTypes() throws Exception {
        ResultActions result = mvc.perform(get("/api/jobs/metadata/types"));

        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(JobType.values().length));

        for (JobType type : JobType.values()) {
            result.andExpect(jsonPath("$[?(@=='" + type.name() + "')]").exists());
        }
    }

    @Test
    void getJobStatuses_ShouldReturnAllJobStatuses() throws Exception {
        ResultActions result = mvc.perform(get("/api/jobs/metadata/statuses"));

        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(JobStatus.values().length));

        for (JobStatus status : JobStatus.values()) {
            result.andExpect(jsonPath("$[?(@=='" + status.name() + "')]").exists());
        }
    }

    @Test
    void getJobTypes_ShouldIncludeSpecificTypes() throws Exception {
        mvc.perform(get("/api/jobs/metadata/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@=='OIL_CHANGE')]").exists())
                .andExpect(jsonPath("$[?(@=='BRAKE_SERVICE')]").exists())
                .andExpect(jsonPath("$[?(@=='GENERAL_MAINTENANCE')]").exists())
                .andExpect(jsonPath("$[?(@=='OTHER')]").exists());
    }

    @Test
    void getJobStatuses_ShouldIncludeSpecificStatuses() throws Exception {
        mvc.perform(get("/api/jobs/metadata/statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@=='WAITING')]").exists())
                .andExpect(jsonPath("$[?(@=='IN_PROGRESS')]").exists())
                .andExpect(jsonPath("$[?(@=='DONE')]").exists());
    }
}
