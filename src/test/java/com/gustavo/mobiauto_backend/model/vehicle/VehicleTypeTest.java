package com.gustavo.mobiauto_backend.model.vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("VehicleType Enum Tests")
class VehicleTypeTest {

    @Test
    @DisplayName("Should have correct enum values")
    void shouldHaveCorrectEnumValues() {
        assertEquals("carro", VehicleType.CAR.getValue());
        assertEquals("motocicleta", VehicleType.MOTORCYCLE.getValue());
        assertEquals("caminhão", VehicleType.TRUCK.getValue());
        assertEquals("outro", VehicleType.OTHER.getValue());
    }

    @Test
    @DisplayName("Should convert string to enum correctly")
    void shouldConvertStringToEnumCorrectly() {
        assertEquals(VehicleType.CAR, VehicleType.valueOf("CAR"));
        assertEquals(VehicleType.MOTORCYCLE, VehicleType.valueOf("MOTORCYCLE"));
        assertEquals(VehicleType.TRUCK, VehicleType.valueOf("TRUCK"));
        assertEquals(VehicleType.OTHER, VehicleType.valueOf("OTHER"));
    }

    @Test
    @DisplayName("Should throw exception for invalid enum value")
    void shouldThrowExceptionForInvalidEnumValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            VehicleType.valueOf("INVALID");
        });
    }
}