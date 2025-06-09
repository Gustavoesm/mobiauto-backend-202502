package com.gustavo.mobiauto_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.mobiauto_backend.controller.dto.AuthResponseDto;
import com.gustavo.mobiauto_backend.controller.dto.UserDto;
import com.gustavo.mobiauto_backend.controller.requests.LoginRequest;
import com.gustavo.mobiauto_backend.controller.requests.UserRequest;
import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.service.TokenService;
import com.gustavo.mobiauto_backend.service.UserService;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserController(UserService userService, AuthenticationManager authenticationManager,
            TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequest request) {
        var credentials = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var auth = this.authenticationManager.authenticate(credentials);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return new ResponseEntity<>(AuthResponseDto.of(token), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRequest request) {
        return new ResponseEntity<>(UserDto.of(userService.registerUser(request)), HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(UserDto.of(userService.findUser(id)), HttpStatus.OK);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return new ResponseEntity<>(UserDto.of(userService.updateUser(id, request)), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/users/{id}/reactivate")
    public ResponseEntity<UserDto> reactivateUser(@PathVariable Long id) {
        return new ResponseEntity<>(UserDto.of(userService.reactivateUser(id)), HttpStatus.OK);
    }
}
