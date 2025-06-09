package com.gustavo.mobiauto_backend.model.vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("VehicleColor Tests")
class VehicleColorTest {

    @Test
    @DisplayName("Should create VehicleColor with valid color")
    void shouldCreateVehicleColorWithValidColor() {
        String colorValue = "Blue";
        VehicleColor vehicleColor = new VehicleColor(colorValue);

        assertNotNull(vehicleColor);
        assertEquals(colorValue, vehicleColor.getValue());
    }

    @Test
    @DisplayName("Should create VehicleColor with no-args constructor")
    void shouldCreateVehicleColorWithNoArgsConstructor() {
        VehicleColor vehicleColor = new VehicleColor();

        assertNotNull(vehicleColor);
        assertNull(vehicleColor.getValue());
    }

    @Test
    @DisplayName("Should set and get color value correctly")
    void shouldSetAndGetColorValueCorrectly() {
        VehicleColor vehicleColor = new VehicleColor();
        String colorValue = "Red";

        vehicleColor.setValue(colorValue);

        assertEquals(colorValue, vehicleColor.getValue());
    }

    @Test
    @DisplayName("Should throw exception for null color value")
    void shouldThrowExceptionForNullColorValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new VehicleColor(null));

        assertEquals("Vehicle color cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty string color value")
    void shouldThrowExceptionForEmptyStringColorValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new VehicleColor(""));

        assertEquals("Vehicle color cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for whitespace only color value")
    void shouldThrowExceptionForWhitespaceOnlyColorValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new VehicleColor("   "));

        assertEquals("Vehicle color cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should trim whitespace from color value")
    void shouldTrimWhitespaceFromColorValue() {
        String colorWithWhitespace = "  Blue  ";
        VehicleColor vehicleColor = new VehicleColor(colorWithWhitespace);

        assertEquals("Blue", vehicleColor.getValue());
    }

    @Test
    @DisplayName("Should handle different color formats")
    void shouldHandleDifferentColorFormats() {
        VehicleColor simpleColor = new VehicleColor("Blue");
        assertEquals("Blue", simpleColor.getValue());

        VehicleColor colorWithSpaces = new VehicleColor("Dark Blue");
        assertEquals("Dark Blue", colorWithSpaces.getValue());

        VehicleColor colorWithSpecialChars = new VehicleColor("Blue-Green");
        assertEquals("Blue-Green", colorWithSpecialChars.getValue());
    }

    @Test
    @DisplayName("Should handle case sensitivity")
    void shouldHandleCaseSensitivity() {
        VehicleColor lowerCase = new VehicleColor("blue");
        VehicleColor upperCase = new VehicleColor("BLUE");
        VehicleColor mixedCase = new VehicleColor("Blue");

        assertEquals("blue", lowerCase.getValue());
        assertEquals("BLUE", upperCase.getValue());
        assertEquals("Blue", mixedCase.getValue());

        assertNotEquals(lowerCase.getValue(), upperCase.getValue());
        assertNotEquals(lowerCase.getValue(), mixedCase.getValue());
        assertNotEquals(upperCase.getValue(), mixedCase.getValue());
    }
}