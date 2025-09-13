package com.mechtrack.controller;

import com.mechtrack.AbstractMechtrackMvcTest;
import com.mechtrack.service.JobService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.mechtrack.provider.JobTestDataProvider.createJobRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JobControllerTest extends AbstractMechtrackMvcTest {

    @Autowired
    private JobService jobService;

    @Test
    @DisplayName("Test: create and retrieve job")
    void createAndRetrieveJob() throws Exception {
        var createJobRequest = createJobRequest();
        
        var result = mvc.perform(
                        post(JOBS_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(createJobRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName", equalTo(createJobRequest.getCustomerName())))
                .andExpect(jsonPath("$.carModel", equalTo(createJobRequest.getCarModel())))
                .andExpect(jsonPath("$.description", equalTo(createJobRequest.getDescription())))
                .andExpect(jsonPath("$.income", equalTo(createJobRequest.getIncome().doubleValue())))
                .andReturn();

        var response = result.getResponse().getContentAsString();
        var jobId = objectMapper.readTree(response).get("id").asText();

        mvc.perform(
                        get(JOBS_URL + "/{id}", jobId)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(jobId)))
                .andExpect(jsonPath("$.customerName", equalTo(createJobRequest.getCustomerName())))
                .andExpect(jsonPath("$.carModel", equalTo(createJobRequest.getCarModel())));
    }

    @Test
    @DisplayName("Test: search jobs by customer name")
    void searchJobsByCustomerName() throws Exception {
        var job = jobService.createJob(createJobRequest("John Smith", "Toyota Camry", "Brake repair", createJobRequest().getDate(), createJobRequest().getIncome()));

        mvc.perform(
                        get(JOBS_URL + "/search")
                                .param("customer", "John")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo(job.getId().toString())))
                .andExpect(jsonPath("$[0].customerName", equalTo("John Smith")));
    }

    @Test
    @DisplayName("Test: update job")
    void updateJob() throws Exception {
        var job = jobService.createJob(createJobRequest());
        var updateRequest = createJobRequest("Updated Customer", "Updated Car", "Updated Description", createJobRequest().getDate(), createJobRequest().getIncome());

        mvc.perform(
                        put(JOBS_URL + "/{id}", job.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(job.getId().toString())))
                .andExpect(jsonPath("$.customerName", equalTo(updateRequest.getCustomerName())))
                .andExpect(jsonPath("$.carModel", equalTo(updateRequest.getCarModel())));
    }

    @Test
    @DisplayName("Test: delete job")
    void deleteJob() throws Exception {
        var job = jobService.createJob(createJobRequest());

        mvc.perform(
                        delete(JOBS_URL + "/{id}", job.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        mvc.perform(
                        get(JOBS_URL + "/{id}", job.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
} 
