package com.mechtrack.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ========== CUSTOM EXCEPTIONS ==========

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.UNAUTHORIZED, 
            "Authentication Failed", 
            ex.getMessage()
        );
        
        log.warn("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<Map<String, Object>> handleTokenValidationException(TokenValidationException ex) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.UNAUTHORIZED, 
            "Token Validation Failed", 
            ex.getMessage()
        );
        
        log.warn("Token validation failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingRequestHeader(MissingRequestHeaderException ex) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.UNAUTHORIZED,
            "Missing Authorization Header",
            "Authorization header is required"
        );

        log.warn("Missing authorization header: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.NOT_FOUND, 
            "Resource Not Found", 
            ex.getMessage()
        );
        
        response.put("entityType", ex.getEntityType());
        response.put("identifier", ex.getIdentifier());
        
        log.info("Entity not found: {} with ID: {}", ex.getEntityType(), ex.getIdentifier());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidInputException(InvalidInputException ex) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.BAD_REQUEST, 
            "Invalid Input", 
            ex.getMessage()
        );
        
        if (ex.getFieldName() != null) {
            response.put("field", ex.getFieldName());
        }
        
        log.warn("Invalid input: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    // ========== VALIDATION EXCEPTIONS ==========

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseException(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "JSON Parse Error");
        
        String message = ex.getMessage();
        if (message != null && message.contains("cost")) {
            response.put("message", "Invalid cost value format. Please enter a valid positive number.");
            response.put("field", "cost");
        } else if (message != null && message.contains("purchaseDate")) {
            response.put("message", "Invalid date format. Please use YYYY-MM-DD format.");
            response.put("field", "purchaseDate");
        } else {
            response.put("message", "Invalid JSON format in request data");
        }
        
        response.put("originalError", ex.getMessage());
        
        log.warn("JSON parsing failed: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation failed");
        response.put("message", "Validation failed");
        response.put("errors", errors);
        
        log.warn("Validation failed: {}", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Missing required parameter");
        response.put("message", ex.getMessage());
        response.put("parameter", ex.getParameterName());
        response.put("parameterType", ex.getParameterType());
        
        log.warn("Missing required parameter: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            errors.put(fieldName, violation.getMessage());
        });
        
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation failed");
        response.put("message", "Validation failed");
        response.put("errors", errors);
        
        log.warn("Constraint violation: {}", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Invalid parameter type");
        response.put("message", "Invalid parameter type");
        response.put("parameter", ex.getName());
        response.put("expectedType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        response.put("providedValue", ex.getValue());
        
        log.warn("Type mismatch for parameter {}: expected {}, got {}", 
                ex.getName(), ex.getRequiredType(), ex.getValue());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Internal Server Error", 
            ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred"
        );
        
        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Internal Server Error", 
            "An unexpected error occurred"
        );
        
        log.error("Unexpected exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // ========== HELPER METHODS ==========

    private Map<String, Object> createErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        return response;
    }
}
