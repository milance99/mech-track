package com.mechtrack.exception;

/**
 * Exception thrown when input validation fails
 */
public class InvalidInputException extends RuntimeException {
    
    private final String fieldName;
    
    public InvalidInputException(String message) {
        super(message);
        this.fieldName = null;
    }
    
    public InvalidInputException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }
    
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
        this.fieldName = null;
    }
    
    public String getFieldName() {
        return fieldName;
    }
}
