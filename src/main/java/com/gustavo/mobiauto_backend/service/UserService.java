package com.gustavo.mobiauto_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.mobiauto_backend.controller.requests.UserRequest;
import com.gustavo.mobiauto_backend.infra.exceptions.UserNotFoundException;
import com.gustavo.mobiauto_backend.infra.repositories.UserRepository;
import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.model.user.UserEmail;
import com.gustavo.mobiauto_backend.model.user.UserName;
import com.gustavo.mobiauto_backend.model.user.UserPassword;
import com.gustavo.mobiauto_backend.service.exceptions.UserAlreadyActiveException;
import com.gustavo.mobiauto_backend.service.exceptions.UserAlreadyDeactivatedException;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerUser(UserRequest request) {
        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword());
        user = userRepository.save(user);
        return user;
    }

    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));
    }

    @Transactional
    public User updateUser(Long id, UserRequest request) {
        User user = this.findUser(id);

        if (request.getFirstName() != null || request.getLastName() != null) {
            UserName name = user.getName();
            String firstName = request.getFirstName() != null ? request.getFirstName() : name.getFirstName();
            String lastName = request.getLastName() != null ? request.getLastName() : name.getLastName();
            user.setName(new UserName(firstName, lastName));
        }

        if (request.getEmail() != null) {
            user.setEmail(new UserEmail(request.getEmail()));
        }

        if (request.getPassword() != null) {
            user.setPassword(new UserPassword(request.getPassword()));
        }

        user = userRepository.save(user);
        return user;
    }

    @Transactional
    public User deactivateUser(Long id) {
        User user = this.findUser(id);

        if (!user.isActive()) {
            throw new UserAlreadyDeactivatedException(id);
        }

        user.setActive(false);
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    public User reactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (user.isActive()) {
            throw new UserAlreadyActiveException(id);
        }

        user.setActive(true);
        user = userRepository.save(user);
        return user;
    }
}