package com.gustavo.mobiauto_backend.model.exceptions;

public class DisabledUserException extends RuntimeException {
    public DisabledUserException(Long userId) {
        super("User with ID " + userId + " is disabled.");
    }
}
