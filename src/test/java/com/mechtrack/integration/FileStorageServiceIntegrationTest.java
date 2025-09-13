package com.mechtrack.integration;

import com.mechtrack.AbstractMechtrackTest;
import com.mechtrack.service.FileStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestPropertySource(properties = {
    "mechtrack.file.upload-dir=./test-uploads"
})
class FileStorageServiceIntegrationTest extends AbstractMechtrackTest {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    @DisplayName("Should store file successfully")
    void shouldStoreFileSuccessfully() {
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test-invoice.pdf", 
                "application/pdf", 
                "Test file content".getBytes()
        );

        String storedFileName = fileStorageService.store(file, "test");

        assertThat(storedFileName).isNotBlank();
        assertThat(storedFileName).contains("test-invoice.pdf");
        
        assertThat(fileStorageService.exists(storedFileName)).isTrue();
    }

    @Test
    @DisplayName("Should reject file with invalid extension")
    void shouldRejectFileWithInvalidExtension() {
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test-script.exe", 
                "application/octet-stream", 
                "Malicious content".getBytes()
        );

        assertThatThrownBy(() -> fileStorageService.store(file, "test"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid file type. Allowed extensions are:");
    }

    @Test
    @DisplayName("Should reject file that is too large")
    void shouldRejectFileThatIsTooLarge() {
        byte[] largeContent = new byte[10 * 1024 * 1024 + 1];
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "large-file.pdf", 
                "application/pdf", 
                largeContent
        );

        assertThatThrownBy(() -> fileStorageService.store(file, "test"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("File size exceeds the maximum limit");
    }

    @Test
    @DisplayName("Should load file successfully")
    void shouldLoadFileSuccessfully() {
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test-document.pdf", 
                "application/pdf", 
                "Test content for loading".getBytes()
        );

        String storedFileName = fileStorageService.store(file, "test");
        var loadedResource = fileStorageService.load(storedFileName);

        assertThat(loadedResource).isNotNull();
        assertThat(loadedResource.exists()).isTrue();
        assertThat(loadedResource.isReadable()).isTrue();
    }

    @Test
    @DisplayName("Should throw exception when loading non-existent file")
    void shouldThrowExceptionWhenLoadingNonExistentFile() {
        assertThatThrownBy(() -> fileStorageService.load("non-existent-file.pdf"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Could not read file");
    }

    @Test
    @DisplayName("Should delete file successfully")
    void shouldDeleteFileSuccessfully() {
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "file-to-delete.pdf", 
                "application/pdf", 
                "Content to be deleted".getBytes()
        );

        String storedFileName = fileStorageService.store(file, "test");
        
        assertThat(fileStorageService.exists(storedFileName)).isTrue();
        
        boolean deleted = fileStorageService.delete(storedFileName);
        
        assertThat(deleted).isTrue();
        assertThat(fileStorageService.exists(storedFileName)).isFalse();
    }

    @Test
    @DisplayName("Should handle file with special characters in name")
    void shouldHandleFileWithSpecialCharactersInName() {
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test file with spaces & symbols!.pdf", 
                "application/pdf", 
                "Content with special name".getBytes()
        );

        String storedFileName = fileStorageService.store(file, "test");

        assertThat(storedFileName).isNotBlank();
        assertThat(storedFileName).contains("test file with spaces & symbols!.pdf");
        assertThat(fileStorageService.exists(storedFileName)).isTrue();
    }
}
