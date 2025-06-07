package com.gustavo.mobiauto_backend.model.exceptions;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(Long id) {
        super("Store with id " + id + " not found.");
    }
}