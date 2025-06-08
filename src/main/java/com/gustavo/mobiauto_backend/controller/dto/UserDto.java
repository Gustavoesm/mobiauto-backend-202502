package com.gustavo.mobiauto_backend.controller.dto;

import com.gustavo.mobiauto_backend.model.exceptions.DisabledUserException;
import com.gustavo.mobiauto_backend.model.user.User;

import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;

    private UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static UserDto of(User user) {
        if (!user.isEnabled())
            throw new DisabledUserException(user.getId());

        return new UserDto(user.getFullName(), user.getEmail().getValue());
    }
}