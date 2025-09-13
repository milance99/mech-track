package com.mechtrack.service.impl;

import com.mechtrack.exception.InvalidInputException;
import com.mechtrack.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Local disk implementation of FileStorageService
 */
@Service
@Slf4j
public class LocalFileStorageService implements FileStorageService {

    private final Path rootLocation;
    private final long maxFileSize;
    private final List<String> allowedExtensions;

    public LocalFileStorageService(
            @Value("${app.file-storage.location:uploads}") String uploadPath,
            @Value("${app.file-storage.max-size-mb:10}") long maxFileSizeMb,
            @Value("${app.file-storage.allowed-extensions:pdf,png,jpg,jpeg}") String[] allowedExtensions) {
        this.rootLocation = Paths.get(uploadPath);
        this.maxFileSize = maxFileSizeMb * 1024 * 1024;
        this.allowedExtensions = Arrays.asList(allowedExtensions);
        try {
            Files.createDirectories(this.rootLocation);
            log.info("File storage initialized at: {}", this.rootLocation.toAbsolutePath());
            log.info("Max file size: {}MB", maxFileSizeMb);
            log.info("Allowed extensions: {}", this.allowedExtensions);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file, String subDir) {
        if (file.isEmpty()) {
            throw new InvalidInputException("file", "Failed to store empty file.");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (originalFilename.contains("..")) {
            throw new InvalidInputException("file", "Cannot store file with relative path outside current directory.");
        }

        // Validate file size
        if (file.getSize() > this.maxFileSize) {
            throw new InvalidInputException("file", "File size exceeds the maximum limit of " + (this.maxFileSize / (1024 * 1024)) + "MB.");
        }

        // Validate file extension
        String fileExtension = getFileExtension(originalFilename);
        if (fileExtension.isEmpty() || !allowedExtensions.contains(fileExtension.substring(1).toLowerCase())) {
            throw new InvalidInputException("file", "Invalid file type. Allowed extensions are: " + allowedExtensions);
        }

        try {
            Path subDirPath = this.rootLocation.resolve(subDir);
            Files.createDirectories(subDirPath);

            String uniqueFilename = UUID.randomUUID() + "-" + originalFilename;
            
            Path destinationFile = subDirPath.resolve(uniqueFilename);
            
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            
            String relativePath = subDir + "/" + uniqueFilename;
            log.info("File stored successfully: {}", relativePath);
            
            return relativePath;
            
        } catch (IOException e) {
            log.error("Failed to store file: {}", originalFilename, e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public Resource load(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + fileName, e);
        }
    }

    @Override
    public boolean delete(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            boolean deleted = Files.deleteIfExists(file);
            if (deleted) {
                log.info("File deleted successfully: {}", fileName);
            }
            return deleted;
        } catch (IOException e) {
            log.error("Failed to delete file: {}", fileName, e);
            return false;
        }
    }

    @Override
    public boolean exists(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            return Files.exists(file);
        } catch (Exception e) {
            log.error("Error checking file existence: {}", fileName, e);
            return false;
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
}
