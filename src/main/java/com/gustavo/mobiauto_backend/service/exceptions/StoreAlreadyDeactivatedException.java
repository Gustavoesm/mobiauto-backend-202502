package com.gustavo.mobiauto_backend.service.exceptions;

public class StoreAlreadyDeactivatedException extends RuntimeException {
    public StoreAlreadyDeactivatedException(Long storeId) {
        super("Store with ID " + storeId + " is already deactivated.");
    }
}