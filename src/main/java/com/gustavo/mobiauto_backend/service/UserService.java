package com.gustavo.mobiauto_backend.service;

import org.springframework.stereotype.Service;

import com.gustavo.mobiauto_backend.controller.dto.UserDto;
import com.gustavo.mobiauto_backend.controller.requests.RegisterUserRequest;
import com.gustavo.mobiauto_backend.model.repositories.UserRepository;
import com.gustavo.mobiauto_backend.model.user.User;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto registerUser(RegisterUserRequest request) {
        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword());
        user = userRepository.save(user);
        return UserDto.of(user);
    }
}