package com.mechtrack;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public abstract class AbstractMechtrackMvcTest extends AbstractMechtrackTest {

    @Autowired
    protected ObjectMapper objectMapper;
    
    @Autowired
    protected WebApplicationContext context;
    
    protected MockMvc mvc;

    protected static final String JOBS_URL = "/api/jobs";
    protected static final String PARTS_URL = "/api/parts";
    protected static final String JOB_PARTS_URL = "/api/jobs/{jobId}/parts";

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    /**
     * Convert object to JSON string for request bodies
     */
    protected String asJsonString(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
} 