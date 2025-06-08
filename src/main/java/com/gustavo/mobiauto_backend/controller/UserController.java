package com.gustavo.mobiauto_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.mobiauto_backend.controller.dto.UserDto;
import com.gustavo.mobiauto_backend.controller.requests.UserRequest;
import com.gustavo.mobiauto_backend.service.UserService;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRequest request) {
        return new ResponseEntity<>(UserDto.of(userService.registerUser(request)), HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(UserDto.of(userService.getUser(id)), HttpStatus.OK);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return new ResponseEntity<>(UserDto.of(userService.updateUser(id, request)), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserDto> deactivateUser(@PathVariable Long id) {
        return new ResponseEntity<>(UserDto.of(userService.deactivateUser(id)), HttpStatus.OK);
    }

    @PatchMapping("/users/{id}/reactivate")
    public ResponseEntity<UserDto> reactivateUser(@PathVariable Long id) {
        return new ResponseEntity<>(UserDto.of(userService.reactivateUser(id)), HttpStatus.OK);
    }
}
