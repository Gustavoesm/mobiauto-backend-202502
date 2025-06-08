package com.gustavo.mobiauto_backend.service.exceptions;

public class UserAlreadyActiveException extends RuntimeException {
    public UserAlreadyActiveException(Long userId) {
        super("User with ID " + userId + " is already active.");
    }
}