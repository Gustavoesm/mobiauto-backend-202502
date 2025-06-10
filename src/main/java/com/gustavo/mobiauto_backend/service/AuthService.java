package com.gustavo.mobiauto_backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.service.exceptions.DeactivatedUserException;

@Service
public class AuthService implements UserDetailsService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.findByEmail(username);

        if (!user.isActive()) {
            throw new DeactivatedUserException(user.getId());
        }

        return user;
    }

}
