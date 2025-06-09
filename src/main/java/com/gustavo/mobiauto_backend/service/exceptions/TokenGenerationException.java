package com.gustavo.mobiauto_backend.service.exceptions;

import com.gustavo.mobiauto_backend.model.user.User;

public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(User user, Throwable cause) {
        super("Failed to generate token for user with email: " + user.getEmail().getValue(), cause);
    }
}