package com.mechtrack.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for file storage operations.
 * Abstraction layer to support different storage backends (local disk, S3, etc.)
 */
public interface FileStorageService {
    
    /**
     * Store a file in the specified subdirectory
     * @param file The file to store
     * @param subDir Subdirectory within the storage location
     * @return The stored file's path/URL for database storage
     */
    String store(MultipartFile file, String subDir);
    
    /**
     * Load a file as a Resource
     * @param fileName The file name/path to load
     * @return Resource representing the file
     */
    Resource load(String fileName);
    
    /**
     * Delete a file
     * @param fileName The file name/path to delete
     * @return true if successfully deleted, false otherwise
     */
    boolean delete(String fileName);
    
    /**
     * Check if a file exists
     * @param fileName The file name/path to check
     * @return true if file exists, false otherwise
     */
    boolean exists(String fileName);
} 