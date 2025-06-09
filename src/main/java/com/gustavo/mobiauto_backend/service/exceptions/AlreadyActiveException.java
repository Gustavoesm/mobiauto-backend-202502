package com.gustavo.mobiauto_backend.service.exceptions;

public class AlreadyActiveException extends RuntimeException {
    public AlreadyActiveException(Class<?> classEntity, Long entityId) {
        super(classEntity.getSimpleName() + " with ID " + entityId + " is already active.");
    }
}