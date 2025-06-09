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
@DisplayName("UserName Tests")
class UserNameTest {

    @Test
    @DisplayName("Should create UserName with valid first and last names")
    void shouldCreateUserNameWithValidNames() {
        UserName userName = new UserName("John", "Doe");

        assertNotNull(userName);
        assertEquals("John", userName.getFirstName());
        assertEquals("Doe", userName.getLastName());
    }

    @Test
    @DisplayName("Should create UserName using no-args constructor")
    void shouldCreateUserNameUsingNoArgsConstructor() {
        UserName userName = new UserName();

        assertNotNull(userName);
        assertNull(userName.getFirstName());
        assertNull(userName.getLastName());
    }

    @Test
    @DisplayName("Should trim whitespace from first name")
    void shouldTrimWhitespaceFromFirstName() {
        UserName userName = new UserName("  John  ", "Doe");

        assertEquals("John", userName.getFirstName());
    }

    @Test
    @DisplayName("Should trim whitespace from last name")
    void shouldTrimWhitespaceFromLastName() {
        UserName userName = new UserName("John", "  Doe  ");

        assertEquals("Doe", userName.getLastName());
    }

    @Test
    @DisplayName("Should set first name correctly")
    void shouldSetFirstNameCorrectly() {
        UserName userName = new UserName();
        userName.setFirstName("Alice");

        assertEquals("Alice", userName.getFirstName());
    }

    @Test
    @DisplayName("Should set last name correctly")
    void shouldSetLastNameCorrectly() {
        UserName userName = new UserName();
        userName.setLastName("Johnson");

        assertEquals("Johnson", userName.getLastName());
    }

    @Test
    @DisplayName("Should trim whitespace when setting first name")
    void shouldTrimWhitespaceWhenSettingFirstName() {
        UserName userName = new UserName();
        userName.setFirstName("  Alice  ");

        assertEquals("Alice", userName.getFirstName());
    }

    @Test
    @DisplayName("Should trim whitespace when setting last name")
    void shouldTrimWhitespaceWhenSettingLastName() {
        UserName userName = new UserName();
        userName.setLastName("  Johnson  ");

        assertEquals("Johnson", userName.getLastName());
    }

    @Test
    @DisplayName("Should throw exception when first name is null")
    void shouldThrowExceptionWhenFirstNameIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserName(null, "Doe"));

        assertEquals("First name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when last name is null")
    void shouldThrowExceptionWhenLastNameIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserName("John", null));

        assertEquals("Last name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when first name is empty")
    void shouldThrowExceptionWhenFirstNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserName("", "Doe"));

        assertEquals("First name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when last name is empty")
    void shouldThrowExceptionWhenLastNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserName("John", ""));

        assertEquals("Last name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when first name is only whitespace")
    void shouldThrowExceptionWhenFirstNameIsOnlyWhitespace() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserName("   ", "Doe"));

        assertEquals("First name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when last name is only whitespace")
    void shouldThrowExceptionWhenLastNameIsOnlyWhitespace() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new UserName("John", "   "));

        assertEquals("Last name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting null first name")
    void shouldThrowExceptionWhenSettingNullFirstName() {
        UserName userName = new UserName();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userName.setFirstName(null));

        assertEquals("First name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting null last name")
    void shouldThrowExceptionWhenSettingNullLastName() {
        UserName userName = new UserName();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userName.setLastName(null));

        assertEquals("Last name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting empty first name")
    void shouldThrowExceptionWhenSettingEmptyFirstName() {
        UserName userName = new UserName();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userName.setFirstName(""));

        assertEquals("First name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting empty last name")
    void shouldThrowExceptionWhenSettingEmptyLastName() {
        UserName userName = new UserName();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userName.setLastName(""));

        assertEquals("Last name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle special characters in names")
    void shouldHandleSpecialCharactersInNames() {
        UserName userName = new UserName("Jean-Luc", "O'Connor");

        assertEquals("Jean-Luc", userName.getFirstName());
        assertEquals("O'Connor", userName.getLastName());
    }

    @Test
    @DisplayName("Should handle accented characters in names")
    void shouldHandleAccentedCharactersInNames() {
        UserName userName = new UserName("José", "Müller");

        assertEquals("José", userName.getFirstName());
        assertEquals("Müller", userName.getLastName());
    }

    @Test
    @DisplayName("Should handle single character names")
    void shouldHandleSingleCharacterNames() {
        UserName userName = new UserName("A", "B");

        assertEquals("A", userName.getFirstName());
        assertEquals("B", userName.getLastName());
    }

    @Test
    @DisplayName("Should handle long names")
    void shouldHandleLongNames() {
        String longFirstName = "Wolfeschlegelsteinhausenbergerdorff";
        String longLastName = "Adolph-Blaine-Charles-David-Earl-Frederick-Gerald-Hubert-Irvin";

        UserName userName = new UserName(longFirstName, longLastName);

        assertEquals(longFirstName, userName.getFirstName());
        assertEquals(longLastName, userName.getLastName());
    }
}