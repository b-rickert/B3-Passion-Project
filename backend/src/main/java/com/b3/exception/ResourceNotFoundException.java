package com.b3.exception;

/**
 * Thrown when a requested resource is not found
 * Returns 404 Not Found
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %s", resourceName, id));
    }
}