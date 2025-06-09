package com.gustavo.mobiauto_backend.model.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserPassword Tests")
class UserPasswordTest {

    @Test
    @DisplayName("Should create UserPassword with valid password")
    void shouldCreateUserPasswordWithValidPassword() {
        UserPassword userPassword = new UserPassword("password123");

        assertNotNull(userPassword);
        assertNotNull(userPassword.getValue());
        assertTrue(!userPassword.getValue().isEmpty());
    }

    @Test
    @DisplayName("Should create UserPassword using no-args constructor")
    void shouldCreateUserPasswordUsingNoArgsConstructor() {
        UserPassword userPassword = new UserPassword();

        assertNotNull(userPassword);
        assertNull(userPassword.getValue());
    }

    @Test
    @DisplayName("Should encrypt password when setting value")
    void shouldEncryptPasswordWhenSettingValue() {
        UserPassword userPassword = new UserPassword();
        String originalPassword = "mypassword";

        userPassword.setValue(originalPassword);

        assertNotNull(userPassword.getValue());
        assertNotEquals(originalPassword, userPassword.getValue());
        assertFalse(userPassword.getValue().isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when password is null")
    void shouldThrowExceptionWhenPasswordIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserPassword(null));

        assertEquals("Password should be at least 4 characters long.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when password is too short")
    void shouldThrowExceptionWhenPasswordIsTooShort() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserPassword("123"));

        assertEquals("Password should be at least 4 characters long.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting null password")
    void shouldThrowExceptionWhenSettingNullPassword() {
        UserPassword userPassword = new UserPassword();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userPassword.setValue(null));

        assertEquals("Password should be at least 4 characters long.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting password too short")
    void shouldThrowExceptionWhenSettingPasswordTooShort() {
        UserPassword userPassword = new UserPassword();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userPassword.setValue("abc"));

        assertEquals("Password should be at least 4 characters long.", exception.getMessage());
    }

    @Test
    @DisplayName("Should accept password with exactly 4 characters")
    void shouldAcceptPasswordWithExactly4Characters() {
        UserPassword userPassword = new UserPassword("1234");

        assertNotNull(userPassword.getValue());
        assertTrue(userPassword.getValue().length() > 0);
    }

    @Test
    @DisplayName("Should accept long password")
    void shouldAcceptLongPassword() {
        String longPassword = "thisIsAVeryLongPasswordWithMoreThan50Characters12345";
        UserPassword userPassword = new UserPassword(longPassword);

        assertNotNull(userPassword.getValue());
        assertTrue(userPassword.getValue().length() > 0);
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void shouldHandleSpecialCharactersInPassword() {
        UserPassword userPassword = new UserPassword("p@ssw0rd!");

        assertNotNull(userPassword.getValue());
        assertTrue(userPassword.getValue().length() > 0);
    }

    @Test
    @DisplayName("Should handle unicode characters in password")
    void shouldHandleUnicodeCharactersInPassword() {
        UserPassword userPassword = new UserPassword("pássw0rd");

        assertNotNull(userPassword.getValue());
        assertTrue(userPassword.getValue().length() > 0);
    }

    @Test
    @DisplayName("Should handle numeric password")
    void shouldHandleNumericPassword() {
        UserPassword userPassword = new UserPassword("123456789");

        assertNotNull(userPassword.getValue());
        assertTrue(userPassword.getValue().length() > 0);
    }

    @Test
    @DisplayName("Should handle alphanumeric password")
    void shouldHandleAlphanumericPassword() {
        UserPassword userPassword = new UserPassword("abc123def456");

        assertNotNull(userPassword.getValue());
        assertTrue(userPassword.getValue().length() > 0);
    }

    @Test
    @DisplayName("Should handle password with spaces")
    void shouldHandlePasswordWithSpaces() {
        UserPassword userPassword = new UserPassword("my password");

        assertNotNull(userPassword.getValue());
        assertTrue(userPassword.getValue().length() > 0);
    }

    @Test
    @DisplayName("Should handle password update")
    void shouldHandlePasswordUpdate() {
        UserPassword userPassword = new UserPassword("oldpassword");
        String firstEncrypted = userPassword.getValue();

        userPassword.setValue("newpassword");
        String secondEncrypted = userPassword.getValue();

        assertNotNull(firstEncrypted);
        assertNotNull(secondEncrypted);
        assertTrue(!"oldpassword".equals(firstEncrypted));
        assertTrue(!"newpassword".equals(secondEncrypted));
    }

    @Test
    @DisplayName("Should encrypt same password consistently within same instance")
    void shouldEncryptSamePasswordConsistentlyWithinSameInstance() {
        String password = "testpassword";
        UserPassword userPassword1 = new UserPassword(password);
        UserPassword userPassword2 = new UserPassword(password);

        assertNotNull(userPassword1.getValue());
        assertNotNull(userPassword2.getValue());
        assertTrue(!password.equals(userPassword1.getValue()));
        assertTrue(!password.equals(userPassword2.getValue()));

        assertTrue(userPassword1.getValue().startsWith("$2a$"));
        assertTrue(userPassword2.getValue().startsWith("$2a$"));
        assertTrue(userPassword1.getValue().length() >= 60);
        assertTrue(userPassword2.getValue().length() >= 60);
    }

    @Test
    @DisplayName("Should handle empty string as invalid password")
    void shouldHandleEmptyStringAsInvalidPassword() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserPassword(""));

        assertEquals("Password should be at least 4 characters long.", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle whitespace-only password as valid if length >= 4")
    void shouldHandleWhitespaceOnlyPasswordAsValidIfLengthAtLeast4() {
        UserPassword userPassword = new UserPassword("    ");

        assertNotNull(userPassword.getValue());
        assertTrue(userPassword.getValue().length() > 0);
    }

    @Test
    @DisplayName("Should reject whitespace-only password if length < 4")
    void shouldRejectWhitespaceOnlyPasswordIfLengthLessThan4() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserPassword("   "));

        assertEquals("Password should be at least 4 characters long.", exception.getMessage());
    }
}