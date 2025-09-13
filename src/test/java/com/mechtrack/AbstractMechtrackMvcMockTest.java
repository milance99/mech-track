package com.mechtrack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mechtrack.service.JobService;
import com.mechtrack.service.PartService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public abstract class AbstractMechtrackMvcMockTest {

    //will be used for mock texts
    protected static final String JOBS_URL = "/api/jobs";
    protected static final String PARTS_URL = "/api/parts";
    protected static final String JOB_PARTS_URL = "/api/jobs/{jobId}/parts";

    protected MockMvc mockMvc;
    
    @Autowired
    protected ObjectMapper objectMapper;
    
    @Autowired
    protected WebApplicationContext webApplicationContext;

    @MockitoBean
    protected JobService jobService;

    @MockitoBean
    protected PartService partService;

    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }

    protected String asJsonString(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}
