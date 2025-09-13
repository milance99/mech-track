package com.mechtrack;

import com.mechtrack.repository.JobRepository;
import com.mechtrack.repository.PartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractMechtrackTest {

    public static final String AUDIT_USERNAME = "unit.test@mechtrack.com";

    @Autowired
    protected JobRepository jobRepository;
    
    @Autowired
    protected PartRepository partRepository;

    @BeforeEach
    void cleanDb() {
        partRepository.deleteAll();
        partRepository.flush();
        
        jobRepository.deleteAll();
        jobRepository.flush();
    }
}
