package com.gustavo.mobiauto_backend.service.exceptions;

public class DeactivatedUserException extends RuntimeException {
    public DeactivatedUserException(Long userId) {
        super("User with ID " + userId + " is deactivated.");
    }
}
