package com.mechtrack.service;

import com.mechtrack.exception.EntityNotFoundException;
import com.mechtrack.model.dto.CreateJobRequest;
import com.mechtrack.model.dto.JobDto;
import com.mechtrack.model.dto.JobSearchCriteria;
import com.mechtrack.model.dto.PartDto;
import com.mechtrack.model.entity.Job;
import com.mechtrack.model.enums.JobStatus;
import com.mechtrack.repository.JobRepository;
import com.mechtrack.repository.specification.JobSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Job job = createJobFromRequest(request);
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
    public Page<JobDto> searchJobs(JobSearchCriteria criteria, Pageable pageable) {
        Specification<Job> spec = JobSpecifications.withCriteria(criteria);
        return jobRepository.findAll(spec, pageable)
                .map(this::convertToDto);
    }


    public JobDto updateJob(UUID id, CreateJobRequest request) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job", id.toString()));

        updateJobFromRequest(job, request);
        Job savedJob = jobRepository.save(job);
        return convertToDto(savedJob);
    }

    public JobDto updateJobStatus(UUID id, JobStatus status) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job", id.toString()));
        
        job.setStatus(status);
        Job savedJob = jobRepository.save(job);
        return convertToDto(savedJob);
    }

    public void deleteJob(UUID id) {
        if (!jobRepository.existsById(id)) {
            throw new EntityNotFoundException("Job", id.toString());
        }
        jobRepository.deleteById(id);
    }

    private Job createJobFromRequest(CreateJobRequest request) {
        Job job = new Job();
        updateJobFromRequest(job, request);
        return job;
    }

    private void updateJobFromRequest(Job job, CreateJobRequest request) {
        job.setCustomerName(request.getCustomerName());
        job.setCarModel(request.getCarModel());
        job.setDescription(request.getDescription());
        job.setDate(request.getDate());
        job.setIncome(request.getIncome());
        job.setType(request.getType());
    }

    private JobDto convertToDto(Job job) {
        return new JobDto(
                job.getId(),
                job.getCustomerName(),
                job.getCarModel(),
                job.getDescription(),
                job.getDate(),
                job.getIncome(),
                job.getStatus(),
                job.getType(),
                convertPartsToDtos(job)
        );
    }

    private List<PartDto> convertPartsToDtos(Job job) {
        if (job.getParts() == null) {
            return new ArrayList<>();
        }
        
        return job.getParts().stream()
                .map(this::convertPartToDto)
                .collect(Collectors.toList());
    }

    private PartDto convertPartToDto(com.mechtrack.model.entity.Part part) {
        return new PartDto(
                part.getId(),
                part.getName(),
                part.getCost(),
                part.getInvoiceImageUrl(),
                part.getPurchaseDate(),
                part.getJob().getId()
        );
    }
} 
