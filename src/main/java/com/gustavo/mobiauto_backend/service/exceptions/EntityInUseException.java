package com.gustavo.mobiauto_backend.service.exceptions;

public class EntityInUseException extends RuntimeException {
    public EntityInUseException(Class<?> classEntity, Long id) {
        super(classEntity.getSimpleName() + " with ID " + id
                + " cannot be deleted because it's being used by other entities");
    }
}