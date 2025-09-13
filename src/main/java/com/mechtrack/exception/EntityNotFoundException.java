package com.mechtrack.exception;

/**
 * Exception thrown when a requested entity is not found
 */
public class EntityNotFoundException extends RuntimeException {
    
    private final String entityType;
    private final String identifier;
    
    public EntityNotFoundException(String entityType, String identifier) {
        super(String.format("%s not found with identifier: %s", entityType, identifier));
        this.entityType = entityType;
        this.identifier = identifier;
    }
    
    public EntityNotFoundException(String entityType, String identifier, Throwable cause) {
        super(String.format("%s not found with identifier: %s", entityType, identifier), cause);
        this.entityType = entityType;
        this.identifier = identifier;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public String getIdentifier() {
        return identifier;
    }
}
