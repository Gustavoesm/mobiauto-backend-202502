package com.gustavo.mobiauto_backend.service.exceptions;

public class UserAlreadyDeactivatedException extends RuntimeException {
    public UserAlreadyDeactivatedException(Long userId) {
        super("User with ID " + userId + " is already deactivated.");
    }
}