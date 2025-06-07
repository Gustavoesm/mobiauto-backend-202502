package com.gustavo.mobiauto_backend.model.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found.");
    }
}
