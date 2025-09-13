package com.mechtrack.service;

import com.mechtrack.exception.EntityNotFoundException;
import com.mechtrack.model.dto.CreateJobRequest;
import com.mechtrack.model.dto.JobDto;
import com.mechtrack.model.dto.JobSearchCriteria;
import com.mechtrack.model.dto.PartDto;
import com.mechtrack.model.entity.Job;
import com.mechtrack.repository.JobRepository;
import com.mechtrack.repository.specification.JobSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JobService {

    private final JobRepository jobRepository;

    public JobDto createJob(CreateJobRequest request) {
        Job job = new Job();
        job.setCustomerName(request.getCustomerName());
        job.setCarModel(request.getCarModel());
        job.setDescription(request.getDescription());
        job.setDate(request.getDate());
        job.setIncome(request.getIncome());

        Job savedJob = jobRepository.save(job);
        return convertToDto(savedJob);
    }

    @Transactional(readOnly = true)
    public List<JobDto> getAllJobs() {
        return jobRepository.findAllWithParts().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<JobDto> getAllJobs(Pageable pageable) {
        return jobRepository.findAllWithParts(pageable)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public JobDto getJobById(UUID id) {
        Job job = jobRepository.findByIdWithParts(id);
        if (job == null) {
            throw new EntityNotFoundException("Job", id.toString());
        }
        return convertToDto(job);
    }

    @Transactional(readOnly = true)
    public List<JobDto> searchJobs(String customerName, String carModel, LocalDate fromDate, LocalDate toDate, BigDecimal minIncome, BigDecimal maxIncome) {
        JobSearchCriteria criteria = new JobSearchCriteria();
        criteria.setCustomerName(customerName);
        criteria.setCarModel(carModel);
        criteria.setStartDate(fromDate);
        criteria.setEndDate(toDate);
        criteria.setMinIncome(minIncome);
        criteria.setMaxIncome(maxIncome);
        
        Specification<Job> spec = JobSpecifications.withCriteria(criteria);
        return jobRepository.findAll(spec).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<JobDto> searchJobs(JobSearchCriteria criteria, Pageable pageable) {
        Specification<Job> spec = JobSpecifications.withCriteria(criteria);
        return jobRepository.findAll(spec, pageable)
                .map(this::convertToDto);
    }


    public JobDto updateJob(UUID id, CreateJobRequest request) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job", id.toString()));

        job.setCustomerName(request.getCustomerName());
        job.setCarModel(request.getCarModel());
        job.setDescription(request.getDescription());
        job.setDate(request.getDate());
        job.setIncome(request.getIncome());

        Job savedJob = jobRepository.save(job);
        return convertToDto(savedJob);
    }

    public void deleteJob(UUID id) {
        if (!jobRepository.existsById(id)) {
            throw new EntityNotFoundException("Job", id.toString());
        }
        jobRepository.deleteById(id);
    }

    private JobDto convertToDto(Job job) {
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setCustomerName(job.getCustomerName());
        dto.setCarModel(job.getCarModel());
        dto.setDescription(job.getDescription());
        dto.setDate(job.getDate());
        dto.setIncome(job.getIncome());

        if (job.getParts() != null) {
            List<PartDto> partDtos = job.getParts().stream()
                    .map(part -> {
                        PartDto partDto = new PartDto();
                        partDto.setId(part.getId());
                        partDto.setName(part.getName());
                        partDto.setCost(part.getCost());
                        partDto.setInvoiceImageUrl(part.getInvoiceImageUrl());
                        partDto.setPurchaseDate(part.getPurchaseDate());
                        partDto.setJobId(job.getId());
                        return partDto;
                    })
                    .collect(Collectors.toList());
            dto.setParts(partDtos);
        } else {
            dto.setParts(new ArrayList<>());
        }

        return dto;
    }
} 
