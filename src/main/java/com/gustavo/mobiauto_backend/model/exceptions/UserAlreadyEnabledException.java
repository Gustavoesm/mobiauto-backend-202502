package com.gustavo.mobiauto_backend.model.exceptions;

public class UserAlreadyEnabledException extends RuntimeException {
    public UserAlreadyEnabledException(Long userId) {
        super("User with ID " + userId + " is already enabled.");
    }
}