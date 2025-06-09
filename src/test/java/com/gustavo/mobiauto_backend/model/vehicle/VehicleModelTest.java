package com.gustavo.mobiauto_backend.model.vehicle;

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
@DisplayName("VehicleModel Tests")
class VehicleModelTest {

    @Test
    @DisplayName("Should create VehicleModel with valid value")
    void shouldCreateVehicleModelWithValidValue() {
        String modelValue = "Toyota Corolla";
        VehicleModel vehicleModel = new VehicleModel(modelValue);

        assertNotNull(vehicleModel);
        assertEquals(modelValue, vehicleModel.getValue());
    }

    @Test
    @DisplayName("Should create VehicleModel with no-args constructor")
    void shouldCreateVehicleModelWithNoArgsConstructor() {
        VehicleModel vehicleModel = new VehicleModel();

        assertNotNull(vehicleModel);
        assertNull(vehicleModel.getValue());
    }

    @Test
    @DisplayName("Should set and get value correctly")
    void shouldSetAndGetValueCorrectly() {
        VehicleModel vehicleModel = new VehicleModel();
        String modelValue = "Honda Civic";

        vehicleModel.setValue(modelValue);

        assertEquals(modelValue, vehicleModel.getValue());
    }

    @Test
    @DisplayName("Should throw exception for null value")
    void shouldThrowExceptionForNullValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new VehicleModel(null));

        assertEquals("Vehicle model cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty string value")
    void shouldThrowExceptionForEmptyStringValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new VehicleModel(""));

        assertEquals("Vehicle model cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for whitespace only value")
    void shouldThrowExceptionForWhitespaceOnlyValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new VehicleModel("   "));

        assertEquals("Vehicle model cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should trim whitespace from model value")
    void shouldTrimWhitespaceFromModelValue() {
        String modelWithWhitespace = "  Toyota Corolla  ";
        VehicleModel vehicleModel = new VehicleModel(modelWithWhitespace);

        assertEquals("Toyota Corolla", vehicleModel.getValue());
    }

    @Test
    @DisplayName("Should handle special characters in model name")
    void shouldHandleSpecialCharactersInModelName() {
        String modelWithSpecialChars = "BMW X5 2.0d xDrive25d";
        VehicleModel vehicleModel = new VehicleModel(modelWithSpecialChars);

        assertEquals(modelWithSpecialChars, vehicleModel.getValue());
    }
}