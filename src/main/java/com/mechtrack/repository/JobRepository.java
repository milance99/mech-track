package com.mechtrack.repository;

import com.mechtrack.model.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID>, JpaSpecificationExecutor<Job> {

    // Find jobs by customer name (case-insensitive)
    List<Job> findByCustomerNameContainingIgnoreCase(String customerName);

    // Find jobs by car model (case-insensitive)
    List<Job> findByCarModelContainingIgnoreCase(String carModel);

    // Find jobs by date range
    List<Job> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // Find jobs by date (specific date)
    List<Job> findByDate(LocalDate date);

    // Find jobs with fetch join for parts to avoid N+1 queries
    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.parts WHERE j.id = :id")
    Job findByIdWithParts(@Param("id") UUID id);
    
    // Find all jobs with parts (optimized for N+1 prevention)
    @Query("SELECT DISTINCT j FROM Job j LEFT JOIN FETCH j.parts ORDER BY j.date DESC")
    List<Job> findAllWithParts();
    
    // Find all jobs with parts - paginated version
    @Query(value = "SELECT DISTINCT j FROM Job j LEFT JOIN FETCH j.parts",
           countQuery = "SELECT COUNT(DISTINCT j) FROM Job j")
    Page<Job> findAllWithParts(Pageable pageable);
    
    // Optimized search queries with fetch joins
    @Query("SELECT DISTINCT j FROM Job j LEFT JOIN FETCH j.parts WHERE UPPER(j.customerName) LIKE UPPER(CONCAT('%', :customerName, '%'))")
    List<Job> findByCustomerNameContainingIgnoreCaseWithParts(@Param("customerName") String customerName);
    
    @Query("SELECT DISTINCT j FROM Job j LEFT JOIN FETCH j.parts WHERE j.date BETWEEN :startDate AND :endDate ORDER BY j.date DESC")
    List<Job> findByDateBetweenWithParts(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
} 