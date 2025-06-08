package com.gustavo.mobiauto_backend.service.exceptions;

public class StoreAlreadyActiveException extends RuntimeException {
    public StoreAlreadyActiveException(Long storeId) {
        super("Store with ID " + storeId + " is already active.");
    }
}