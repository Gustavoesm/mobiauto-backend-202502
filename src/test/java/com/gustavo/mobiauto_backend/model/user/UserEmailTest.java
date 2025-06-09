package com.gustavo.mobiauto_backend.model.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserEmail Tests")
class UserEmailTest {

    @Test
    @DisplayName("Should create UserEmail with valid email")
    void shouldCreateUserEmailWithValidEmail() {
        UserEmail userEmail = new UserEmail("john.doe@example.com");

        assertNotNull(userEmail);
        assertEquals("john.doe@example.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should create UserEmail using no-args constructor")
    void shouldCreateUserEmailUsingNoArgsConstructor() {
        UserEmail userEmail = new UserEmail();

        assertNotNull(userEmail);
        assertNull(userEmail.getValue());
    }

    @Test
    @DisplayName("Should trim and convert email to lowercase")
    void shouldTrimAndConvertEmailToLowercase() {
        UserEmail userEmail = new UserEmail("  JOHN.DOE@EXAMPLE.COM  ");

        assertEquals("john.doe@example.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should set email correctly")
    void shouldSetEmailCorrectly() {
        UserEmail userEmail = new UserEmail();
        userEmail.setValue("alice.johnson@test.com");

        assertEquals("alice.johnson@test.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should trim and convert when setting email")
    void shouldTrimAndConvertWhenSettingEmail() {
        UserEmail userEmail = new UserEmail();
        userEmail.setValue("  ALICE.JOHNSON@TEST.COM  ");

        assertEquals("alice.johnson@test.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should throw exception when email is null")
    void shouldThrowExceptionWhenEmailIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserEmail(null));

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when email is empty")
    void shouldThrowExceptionWhenEmailIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserEmail(""));

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when email is only whitespace")
    void shouldThrowExceptionWhenEmailIsOnlyWhitespace() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserEmail("   "));

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting null email")
    void shouldThrowExceptionWhenSettingNullEmail() {
        UserEmail userEmail = new UserEmail();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userEmail.setValue(null));

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting empty email")
    void shouldThrowExceptionWhenSettingEmptyEmail() {
        UserEmail userEmail = new UserEmail();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userEmail.setValue(""));

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for invalid email format - missing @")
    void shouldThrowExceptionForInvalidEmailFormatMissingAt() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserEmail("john.doeexample.com"));

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for invalid email format - missing domain")
    void shouldThrowExceptionForInvalidEmailFormatMissingDomain() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserEmail("john.doe@"));

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for invalid email format - missing local part")
    void shouldThrowExceptionForInvalidEmailFormatMissingLocalPart() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserEmail("@example.com"));

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for invalid email format - multiple @")
    void shouldThrowExceptionForInvalidEmailFormatMultipleAt() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserEmail("john@doe@example.com"));

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for invalid email format - spaces in email")
    void shouldThrowExceptionForInvalidEmailFormatSpacesInEmail() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserEmail("john doe@example.com"));

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should accept valid email with numbers")
    void shouldAcceptValidEmailWithNumbers() {
        UserEmail userEmail = new UserEmail("user123@example123.com");

        assertEquals("user123@example123.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should accept valid email with dots in local part")
    void shouldAcceptValidEmailWithDotsInLocalPart() {
        UserEmail userEmail = new UserEmail("first.last@example.com");

        assertEquals("first.last@example.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should accept valid email with plus sign")
    void shouldAcceptValidEmailWithPlusSign() {
        UserEmail userEmail = new UserEmail("user+tag@example.com");

        assertEquals("user+tag@example.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should accept valid email with underscore")
    void shouldAcceptValidEmailWithUnderscore() {
        UserEmail userEmail = new UserEmail("user_name@example.com");

        assertEquals("user_name@example.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should accept valid email with different TLD")
    void shouldAcceptValidEmailWithDifferentTLD() {
        UserEmail userEmail = new UserEmail("user@example.org");

        assertEquals("user@example.org", userEmail.getValue());
    }

    @Test
    @DisplayName("Should handle email update")
    void shouldHandleEmailUpdate() {
        UserEmail userEmail = new UserEmail("old@example.com");
        userEmail.setValue("new@example.com");

        assertEquals("new@example.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should accept valid email with subdomain")
    void shouldAcceptValidEmailWithSubdomain() {
        UserEmail userEmail = new UserEmail("user@mail.example.com");

        assertEquals("user@mail.example.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should accept valid email with hyphen in domain")
    void shouldAcceptValidEmailWithHyphenInDomain() {
        UserEmail userEmail = new UserEmail("user@my-domain.com");

        assertEquals("user@my-domain.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should accept valid email with multiple subdomains")
    void shouldAcceptValidEmailWithMultipleSubdomains() {
        UserEmail userEmail = new UserEmail("user@server.mail.example.com");

        assertEquals("user@server.mail.example.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should accept valid email with hyphen and subdomain")
    void shouldAcceptValidEmailWithHyphenAndSubdomain() {
        UserEmail userEmail = new UserEmail("user@mail-server.example.com");

        assertEquals("user@mail-server.example.com", userEmail.getValue());
    }

    @Test
    @DisplayName("Should handle case sensitivity correctly")
    void shouldHandleCaseSensitivityCorrectly() {
        UserEmail userEmail1 = new UserEmail("User@Example.COM");
        UserEmail userEmail2 = new UserEmail("user@example.com");

        assertEquals(userEmail1.getValue(), userEmail2.getValue());
        assertEquals("user@example.com", userEmail1.getValue());
    }
}