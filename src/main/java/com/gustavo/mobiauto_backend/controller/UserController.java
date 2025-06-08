package com.gustavo.mobiauto_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gustavo.mobiauto_backend.controller.dto.UserDto;
import com.gustavo.mobiauto_backend.controller.requests.RegisterUserRequest;
import com.gustavo.mobiauto_backend.service.UserService;

public class UserController {
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserRequest request) {
        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
    }
}
