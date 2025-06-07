package com.gustavo.mobiauto_backend.controller.dto;

import com.gustavo.mobiauto_backend.model.user.User;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;

    private UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static UserDto of(User user) {
        return new UserDto(user.getId(), user.getFullName(), user.getEmail().getValue());
    }
}