package com.gustavo.mobiauto_backend.service.exceptions;

public class DuplicateException extends RuntimeException {
    public DuplicateException(Class<?> classEntity, String value) {
        super(classEntity.getSimpleName() + " '" + value + "' is already registered");
    }
}