package com.gustavo.mobiauto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.gustavo.mobiauto_backend.infra.exceptions.UserNotFoundException;
import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.model.user.UserEmail;
import com.gustavo.mobiauto_backend.model.user.UserName;
import com.gustavo.mobiauto_backend.model.user.UserPassword;
import com.gustavo.mobiauto_backend.service.exceptions.DeactivatedUserException;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    private User activeUser;
    private User inactiveUser;
    private String validEmail;
    private String invalidEmail;

    @BeforeEach
    void setUp() {
        validEmail = "john.doe@example.com";
        invalidEmail = "nonexistent@example.com";

        activeUser = new User(
                new UserName("John", "Doe"),
                new UserEmail(validEmail),
                new UserPassword("password123"),
                true);
        activeUser.setId(1L);

        inactiveUser = new User(
                new UserName("Jane", "Smith"),
                new UserEmail("jane.smith@example.com"),
                new UserPassword("password456"),
                false);
        inactiveUser.setId(2L);
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void shouldLoadUserByUsernameSuccessfully() {
        when(userService.findByEmail(validEmail)).thenReturn(activeUser);

        UserDetails result = authService.loadUserByUsername(validEmail);

        assertNotNull(result);
        assertEquals(activeUser, result);
        assertEquals(validEmail, result.getUsername());
        assertEquals(activeUser.getPassword(), result.getPassword());
        assertTrue(result.isEnabled());
        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isAccountNonLocked());
        assertTrue(result.isCredentialsNonExpired());

        verify(userService, times(1)).findByEmail(validEmail);
    }

    @Test
    @DisplayName("Should throw DeactivatedUserException when user is inactive")
    void shouldThrowDeactivatedUserExceptionWhenUserIsInactive() {
        String inactiveUserEmail = "jane.smith@example.com";
        when(userService.findByEmail(inactiveUserEmail)).thenReturn(inactiveUser);

        DeactivatedUserException exception = assertThrows(DeactivatedUserException.class, () -> {
            authService.loadUserByUsername(inactiveUserEmail);
        });

        assertNotNull(exception);
        assertEquals("User with ID " + inactiveUser.getId() + " is deactivated.", exception.getMessage());
        verify(userService, times(1)).findByEmail(inactiveUserEmail);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user not found")
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        String expectedMessage = "User with email " + invalidEmail + " not found.";
        when(userService.findByEmail(invalidEmail))
                .thenThrow(new UserNotFoundException(expectedMessage));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authService.loadUserByUsername(invalidEmail);
        });

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        verify(userService, times(1)).findByEmail(invalidEmail);
    }

    @Test
    @DisplayName("Should handle email with different cases for active users")
    void shouldHandleEmailWithDifferentCasesForActiveUsers() {
        String upperCaseEmail = "JOHN.DOE@EXAMPLE.COM";
        when(userService.findByEmail(upperCaseEmail)).thenReturn(activeUser);

        UserDetails result = authService.loadUserByUsername(upperCaseEmail);

        assertNotNull(result);
        assertEquals(activeUser, result);
        verify(userService, times(1)).findByEmail(upperCaseEmail);
    }

    @Test
    @DisplayName("Should handle email with whitespace for active users")
    void shouldHandleEmailWithWhitespaceForActiveUsers() {
        String emailWithSpaces = "  john.doe@example.com  ";
        when(userService.findByEmail(emailWithSpaces)).thenReturn(activeUser);

        UserDetails result = authService.loadUserByUsername(emailWithSpaces);

        assertNotNull(result);
        assertEquals(activeUser, result);
        verify(userService, times(1)).findByEmail(emailWithSpaces);
    }

    @Test
    @DisplayName("Should handle null username gracefully")
    void shouldHandleNullUsernameGracefully() {
        when(userService.findByEmail(null))
                .thenThrow(new UserNotFoundException("User with email null not found."));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authService.loadUserByUsername(null);
        });

        assertNotNull(exception);
        assertEquals("User with email null not found.", exception.getMessage());
        verify(userService, times(1)).findByEmail(null);
    }

    @Test
    @DisplayName("Should handle empty username")
    void shouldHandleEmptyUsername() {
        String emptyEmail = "";
        when(userService.findByEmail(emptyEmail))
                .thenThrow(new UserNotFoundException("User with email  not found."));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authService.loadUserByUsername(emptyEmail);
        });

        assertNotNull(exception);
        assertEquals("User with email  not found.", exception.getMessage());
        verify(userService, times(1)).findByEmail(emptyEmail);
    }

    @Test
    @DisplayName("Should delegate username lookup to UserService findByEmail")
    void shouldDelegateUsernameLoopupToUserServiceFindByEmail() {
        String testEmail = "test@example.com";
        when(userService.findByEmail(testEmail)).thenReturn(activeUser);

        authService.loadUserByUsername(testEmail);

        verify(userService, times(1)).findByEmail(testEmail);
    }

    @Test
    @DisplayName("Should return UserDetails interface implementation for active users")
    void shouldReturnUserDetailsInterfaceImplementationForActiveUsers() {
        when(userService.findByEmail(validEmail)).thenReturn(activeUser);

        UserDetails result = authService.loadUserByUsername(validEmail);

        assertNotNull(result);
        assertTrue(result instanceof UserDetails);
        assertTrue(result instanceof User);

        assertNotNull(result.getUsername());
        assertNotNull(result.getPassword());
        assertNotNull(result.getAuthorities());

        verify(userService, times(1)).findByEmail(validEmail);
    }

    @Test
    @DisplayName("Should throw DeactivatedUserException for inactive users with different emails")
    void shouldThrowDeactivatedUserExceptionForInactiveUsersWithDifferentEmails() {
        User anotherInactiveUser = new User(
                new UserName("Bob", "Wilson"),
                new UserEmail("bob.wilson@example.com"),
                new UserPassword("password789"),
                false);
        anotherInactiveUser.setId(3L);

        when(userService.findByEmail("bob.wilson@example.com")).thenReturn(anotherInactiveUser);

        DeactivatedUserException exception = assertThrows(DeactivatedUserException.class, () -> {
            authService.loadUserByUsername("bob.wilson@example.com");
        });

        assertNotNull(exception);
        assertEquals("User with ID " + anotherInactiveUser.getId() + " is deactivated.", exception.getMessage());
        verify(userService, times(1)).findByEmail("bob.wilson@example.com");
    }

    @Test
    @DisplayName("Should handle special characters in email for active users")
    void shouldHandleSpecialCharactersInEmailForActiveUsers() {
        String specialEmail = "test+tag@sub-domain.example-site.co.uk";
        User userWithSpecialEmail = new User(
                new UserName("Test", "User"),
                new UserEmail("test+tag@sub-domain.example-site.co.uk"),
                new UserPassword("password123"));
        userWithSpecialEmail.setId(3L);

        when(userService.findByEmail(specialEmail)).thenReturn(userWithSpecialEmail);

        UserDetails result = authService.loadUserByUsername(specialEmail);

        assertNotNull(result);
        assertEquals(userWithSpecialEmail, result);
        assertEquals(specialEmail, result.getUsername());

        verify(userService, times(1)).findByEmail(specialEmail);
    }

    @Test
    @DisplayName("Should properly implement UserDetailsService interface for active users")
    void shouldProperlyImplementUserDetailsServiceInterfaceForActiveUsers() {
        when(userService.findByEmail(validEmail)).thenReturn(activeUser);

        UserDetails result = authService.loadUserByUsername(validEmail);

        assertNotNull(result.getAuthorities());
        assertTrue(result.getAuthorities().isEmpty());

        assertEquals(validEmail, result.getUsername());
        assertNotNull(result.getPassword());

        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isAccountNonLocked());
        assertTrue(result.isCredentialsNonExpired());
        assertEquals(activeUser.isActive(), result.isEnabled());

        verify(userService, times(1)).findByEmail(validEmail);
    }
}