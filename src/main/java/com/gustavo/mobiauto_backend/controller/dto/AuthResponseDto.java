package com.gustavo.mobiauto_backend.controller.dto;

public record AuthResponseDto(String token) {
    public static AuthResponseDto of(String token) {
        return new AuthResponseDto(token);
    }
}
