package com.mechtrack.repository;

import com.mechtrack.model.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PartRepository extends JpaRepository<Part, UUID>, JpaSpecificationExecutor<Part> {

    // Find parts by job ID - corrected to use job.id relationship path
    List<Part> findByJobId(UUID jobId);

    // Find parts by name (case-insensitive)
    List<Part> findByNameContainingIgnoreCase(String name);
    
    // Find parts by name and job ID - corrected to use job.id relationship path
    List<Part> findByNameContainingIgnoreCaseAndJobId(String name, UUID jobId);
    
    // Find parts by purchase date range
    List<Part> findByPurchaseDateBetween(LocalDate startDate, LocalDate endDate);

    // Alternative explicit queries for clarity
    @Query("SELECT p FROM Part p WHERE p.job.id = :jobId")
    List<Part> findPartsByJobId(@Param("jobId") UUID jobId);

    @Query("SELECT p FROM Part p WHERE UPPER(p.name) LIKE UPPER(CONCAT('%', :name, '%')) AND p.job.id = :jobId")
    List<Part> findPartsByNameAndJobId(@Param("name") String name, @Param("jobId") UUID jobId);
}
