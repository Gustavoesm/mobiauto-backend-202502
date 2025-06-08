package com.gustavo.mobiauto_backend.service.exceptions;

public class DeactivatedStoreException extends RuntimeException {
    public DeactivatedStoreException(Long storeId) {
        super("Store with ID " + storeId + " is deactivated.");
    }
}