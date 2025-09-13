package com.mechtrack.repository;

import com.mechtrack.model.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PartRepository extends JpaRepository<Part, UUID>, JpaSpecificationExecutor<Part> {

    // Find parts by job ID
    List<Part> findByJobId(UUID jobId);

    // Find parts by name (case-insensitive)
    List<Part> findByNameContainingIgnoreCase(String name);
    
    // Find parts by name and job ID
    List<Part> findByNameContainingIgnoreCaseAndJobId(String name, UUID jobId);
    
    // Find parts by purchase date range
    List<Part> findByPurchaseDateBetween(LocalDate startDate, LocalDate endDate);
} 