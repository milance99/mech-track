package com.mechtrack.service;

import com.mechtrack.exception.EntityNotFoundException;
import com.mechtrack.model.dto.CreatePartRequest;
import com.mechtrack.model.dto.PartDto;
import com.mechtrack.model.entity.Job;
import com.mechtrack.model.entity.Part;
import com.mechtrack.repository.JobRepository;
import com.mechtrack.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PartService {

    private final PartRepository partRepository;
    private final JobRepository jobRepository;
    private final FileStorageService fileStorageService;

    // Allowed file types for invoice uploads
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "application/pdf"
    );



    public PartDto addPartToJob(UUID jobId, CreatePartRequest request, MultipartFile file) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job", jobId.toString()));

        Part part = new Part();
        part.setName(request.getName());
        part.setCost(request.getCost());
        part.setPurchaseDate(request.getPurchaseDate());
        part.setJob(job);

        if (file != null && !file.isEmpty()) {
            validateFile(file);
            String storedFileName = fileStorageService.store(file, "invoices");
            part.setInvoiceImageUrl(storedFileName);
            log.info("Invoice image uploaded for part: {}", storedFileName);
        }

        Part savedPart = partRepository.save(part);
        log.info("Part created successfully: {} for job: {}", savedPart.getId(), jobId);
        return convertToDto(savedPart);
    }

    @Transactional(readOnly = true)
    public List<PartDto> getAllParts() {
        return partRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<PartDto> getAllParts(Pageable pageable) {
        return partRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public PartDto getPartById(UUID id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Part", id.toString()));
        return convertToDto(part);
    }

    @Transactional(readOnly = true)
    public List<PartDto> searchParts(String name, UUID jobId) {
        if (name != null && jobId != null) {
            // Search by both name and job
            return partRepository.findByNameContainingIgnoreCaseAndJobId(name, jobId)
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else if (name != null) {
            // Search by name only
            return partRepository.findByNameContainingIgnoreCase(name)
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else if (jobId != null) {
            // Search by job only
            return getPartsByJobId(jobId);
        } else {
            // Return all parts if no criteria
            return getAllParts();
        }
    }

    @Transactional(readOnly = true)
    public List<PartDto> getPartsByJobId(UUID jobId) {
        return partRepository.findByJobId(jobId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public PartDto updatePart(UUID id, CreatePartRequest request) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Part", id.toString()));

        part.setName(request.getName());
        part.setCost(request.getCost());
        part.setInvoiceImageUrl(request.getInvoiceImageUrl());
        part.setPurchaseDate(request.getPurchaseDate());

        Part savedPart = partRepository.save(part);
        return convertToDto(savedPart);
    }

    public PartDto uploadPartInvoice(String partId, MultipartFile file) {
        UUID id;
        try {
            id = UUID.fromString(partId);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid part ID format: " + partId);
        }
        
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Part", partId));

        validateFile(file);

        if (part.getInvoiceImageUrl() != null &&
            !part.getInvoiceImageUrl().startsWith("http") && 
            fileStorageService.exists(part.getInvoiceImageUrl())) {
            
            boolean deleted = fileStorageService.delete(part.getInvoiceImageUrl());
            if (deleted) {
                log.info("Deleted old invoice file: {}", part.getInvoiceImageUrl());
            }
        }

        String filePath = fileStorageService.store(file, "invoices");
        part.setInvoiceImageUrl(filePath);
        
        Part savedPart = partRepository.save(part);
        log.info("Uploaded invoice file for part {}: {}", partId, filePath);
        
        return convertToDto(savedPart);
    }

    public void deletePart(UUID id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Part", id.toString()));
        
        if (part.getInvoiceImageUrl() != null &&
            !part.getInvoiceImageUrl().startsWith("http") && 
            fileStorageService.exists(part.getInvoiceImageUrl())) {
            
            boolean deleted = fileStorageService.delete(part.getInvoiceImageUrl());
            if (deleted) {
                log.info("Deleted invoice file: {}", part.getInvoiceImageUrl());
            }
        }
        
        partRepository.deleteById(id);
    }

    private void validateFile(MultipartFile file) {
        long maxSizeBytes = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSizeBytes) {
            throw new RuntimeException("File size exceeds maximum allowed size of 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_FILE_TYPES.contains(contentType.toLowerCase())) {
            throw new RuntimeException("Invalid file type. Allowed types: JPG, PNG, PDF");
        }

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
    }

    private PartDto convertToDto(Part part) {
        return new PartDto(
                part.getId(),
                part.getName(),
                part.getCost(),
                part.getInvoiceImageUrl(),
                part.getPurchaseDate(),
                part.getJob() != null ? part.getJob().getId() : null
        );
    }
}
