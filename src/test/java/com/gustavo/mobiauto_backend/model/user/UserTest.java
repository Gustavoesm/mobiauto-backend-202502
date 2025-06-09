package com.gustavo.mobiauto_backend.model.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("User Tests")
class UserTest {

    private User user;
    private UserName name;
    private UserEmail email;
    private UserPassword password;

    @BeforeEach
    void setUp() {
        name = new UserName("John", "Doe");
        email = new UserEmail("john.doe@example.com");
        password = new UserPassword("password123");
    }

    @Test
    @DisplayName("Should create user with UserName, UserEmail and UserPassword objects")
    void shouldCreateUserWithUserObjects() {
        user = new User(name, email, password);

        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password.getValue(), user.getPassword());
        assertTrue(user.isActive());
        assertNull(user.getId());
    }

    @Test
    @DisplayName("Should create user with UserName, UserEmail, UserPassword and active status")
    void shouldCreateUserWithActiveStatus() {
        user = new User(name, email, password, false);

        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password.getValue(), user.getPassword());
        assertFalse(user.isActive());
        assertNull(user.getId());
    }

    @Test
    @DisplayName("Should create user with string parameters")
    void shouldCreateUserWithStringParameters() {
        user = new User("Jane", "Smith", "jane.smith@example.com", "password456");

        assertNotNull(user);
        assertEquals("Jane", user.getName().getFirstName());
        assertEquals("Smith", user.getName().getLastName());
        assertEquals("jane.smith@example.com", user.getEmail().getValue());
        assertTrue(user.isActive());
        assertNull(user.getId());
    }

    @Test
    @DisplayName("Should create user using no-args constructor")
    void shouldCreateUserUsingNoArgsConstructor() {
        user = new User();

        assertNotNull(user);
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertFalse(user.isActive());
        assertNull(user.getId());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllPropertiesCorrectly() {
        user = new User();
        Long id = 1L;
        UserName newName = new UserName("Alice", "Johnson");
        UserEmail newEmail = new UserEmail("alice.johnson@example.com");
        UserPassword newPassword = new UserPassword("newpassword789");

        user.setId(id);
        user.setName(newName);
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        user.setActive(true);

        assertEquals(id, user.getId());
        assertEquals(newName, user.getName());
        assertEquals(newEmail, user.getEmail());
        assertEquals(newPassword.getValue(), user.getPassword());
        assertTrue(user.isActive());
    }

    @Test
    @DisplayName("Should return correct full name")
    void shouldReturnCorrectFullName() {
        user = new User("John", "Doe", "john.doe@example.com", "password123");

        assertEquals("John Doe", user.getFullName());
    }

    @Test
    @DisplayName("Should implement UserDetails interface correctly")
    void shouldImplementUserDetailsInterfaceCorrectly() {
        user = new User("John", "Doe", "john.doe@example.com", "password123");

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());

        assertNotNull(user.getPassword());
        assertFalse(user.getPassword().isEmpty());

        assertEquals("john.doe@example.com", user.getUsername());

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled()); // Should return active status
    }

    @Test
    @DisplayName("Should return false for isEnabled when user is inactive")
    void shouldReturnFalseForIsEnabledWhenUserIsInactive() {
        user = new User(name, email, password, false);

        assertFalse(user.isEnabled());
    }

    @Test
    @DisplayName("Should handle active status changes")
    void shouldHandleActiveStatusChanges() {
        user = new User("John", "Doe", "john.doe@example.com", "password123");

        assertTrue(user.isActive());
        assertTrue(user.isEnabled());

        user.setActive(false);
        assertFalse(user.isActive());
        assertFalse(user.isEnabled());

        user.setActive(true);
        assertTrue(user.isActive());
        assertTrue(user.isEnabled());
    }

    @Test
    @DisplayName("Should handle name updates")
    void shouldHandleNameUpdates() {
        user = new User("John", "Doe", "john.doe@example.com", "password123");
        UserName newName = new UserName("Jane", "Smith");

        user.setName(newName);

        assertEquals("Jane", user.getName().getFirstName());
        assertEquals("Smith", user.getName().getLastName());
        assertEquals("Jane Smith", user.getFullName());
    }

    @Test
    @DisplayName("Should handle email updates")
    void shouldHandleEmailUpdates() {
        user = new User("John", "Doe", "john.doe@example.com", "password123");
        UserEmail newEmail = new UserEmail("newemail@example.com");

        user.setEmail(newEmail);

        assertEquals("newemail@example.com", user.getEmail().getValue());
        assertEquals("newemail@example.com", user.getUsername());
    }

    @Test
    @DisplayName("Should handle password updates")
    void shouldHandlePasswordUpdates() {
        user = new User("John", "Doe", "john.doe@example.com", "password123");
        UserPassword newPassword = new UserPassword("newpassword456");

        user.setPassword(newPassword);

        assertEquals(newPassword.getValue(), user.getPassword());
    }
}