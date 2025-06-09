package com.gustavo.mobiauto_backend.service.exceptions;

public class AlreadyDeactivatedException extends RuntimeException {
    public AlreadyDeactivatedException(Class<?> classEntity, Long entityId) {
        super(classEntity.getSimpleName() + " with ID " + entityId + " is already deactivated.");
    }
}