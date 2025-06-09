package com.gustavo.mobiauto_backend.model.store;

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
@DisplayName("StoreName Tests")
class StoreNameTest {

    @Test
    @DisplayName("Should create StoreName with valid value")
    void shouldCreateStoreNameWithValidValue() {
        String nameValue = "Loja ABC Veículos";
        StoreName storeName = new StoreName(nameValue);

        assertNotNull(storeName);
        assertEquals(nameValue, storeName.getValue());
    }

    @Test
    @DisplayName("Should create StoreName with no-args constructor")
    void shouldCreateStoreNameWithNoArgsConstructor() {
        StoreName storeName = new StoreName();

        assertNotNull(storeName);
        assertNull(storeName.getValue());
    }

    @Test
    @DisplayName("Should set and get value correctly")
    void shouldSetAndGetValueCorrectly() {
        StoreName storeName = new StoreName();
        String nameValue = "Concessionária XYZ";

        storeName.setValue(nameValue);

        assertEquals(nameValue, storeName.getValue());
    }

    @Test
    @DisplayName("Should throw exception for null value")
    void shouldThrowExceptionForNullValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new StoreName(null));

        assertEquals("Store name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty string value")
    void shouldThrowExceptionForEmptyStringValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new StoreName(""));

        assertEquals("Store name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for whitespace only value")
    void shouldThrowExceptionForWhitespaceOnlyValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new StoreName("   "));

        assertEquals("Store name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should trim whitespace from store name value")
    void shouldTrimWhitespaceFromStoreNameValue() {
        String nameWithWhitespace = "  Loja de Carros Premium  ";
        StoreName storeName = new StoreName(nameWithWhitespace);

        assertEquals("Loja de Carros Premium", storeName.getValue());
    }

    @Test
    @DisplayName("Should handle special characters in store name")
    void shouldHandleSpecialCharactersInStoreName() {
        String nameWithSpecialChars = "Auto Center R&D - Matriz";
        StoreName storeName = new StoreName(nameWithSpecialChars);

        assertEquals(nameWithSpecialChars, storeName.getValue());
    }

    @Test
    @DisplayName("Should handle long store names")
    void shouldHandleLongStoreNames() {
        String longName = "Concessionária de Veículos Automotores e Motocicletas Premium Ltda - Filial Centro";
        StoreName storeName = new StoreName(longName);

        assertEquals(longName, storeName.getValue());
    }

    @Test
    @DisplayName("Should throw exception when setting null value via setter")
    void shouldThrowExceptionWhenSettingNullValueViaSetter() {
        StoreName storeName = new StoreName();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeName.setValue(null));

        assertEquals("Store name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting empty value via setter")
    void shouldThrowExceptionWhenSettingEmptyValueViaSetter() {
        StoreName storeName = new StoreName();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeName.setValue(""));

        assertEquals("Store name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle accented characters")
    void shouldHandleAccentedCharacters() {
        String nameWithAccents = "Concessionária São João";
        StoreName storeName = new StoreName(nameWithAccents);

        assertEquals(nameWithAccents, storeName.getValue());
    }
}