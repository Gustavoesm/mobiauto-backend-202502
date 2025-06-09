package com.gustavo.mobiauto_backend.model.vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("VehicleReleaseYear Tests")
class VehicleReleaseYearTest {

    @Test
    @DisplayName("Should create VehicleReleaseYear with valid year")
    void shouldCreateVehicleReleaseYearWithValidYear() {
        int validYear = 2020;
        VehicleReleaseYear vehicleReleaseYear = new VehicleReleaseYear(validYear);

        assertNotNull(vehicleReleaseYear);
        assertEquals(validYear, vehicleReleaseYear.getValue());
    }

    @Test
    @DisplayName("Should create VehicleReleaseYear with no-args constructor")
    void shouldCreateVehicleReleaseYearWithNoArgsConstructor() {
        VehicleReleaseYear vehicleReleaseYear = new VehicleReleaseYear();

        assertNotNull(vehicleReleaseYear);
        assertEquals(0, vehicleReleaseYear.getValue());
    }

    @Test
    @DisplayName("Should accept minimum valid year (1900)")
    void shouldAcceptMinimumValidYear() {
        int minValidYear = 1900;
        VehicleReleaseYear vehicleReleaseYear = new VehicleReleaseYear(minValidYear);

        assertEquals(minValidYear, vehicleReleaseYear.getValue());
    }

    @Test
    @DisplayName("Should accept current year")
    void shouldAcceptCurrentYear() {
        int currentYear = java.time.Year.now().getValue();
        VehicleReleaseYear vehicleReleaseYear = new VehicleReleaseYear(currentYear);

        assertEquals(currentYear, vehicleReleaseYear.getValue());
    }

    @Test
    @DisplayName("Should throw exception for year before 1900")
    void shouldThrowExceptionForYearBefore1900() {
        int invalidYear = 1899;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new VehicleReleaseYear(invalidYear));

        assertEquals("Vehicle release year must be after 1900. Provided: " + invalidYear,
                exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for future year")
    void shouldThrowExceptionForFutureYear() {
        int futureYear = java.time.Year.now().getValue() + 1;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new VehicleReleaseYear(futureYear));

        assertEquals("Vehicle release year cannot be in the future. Provided: " + futureYear,
                exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting invalid year via setter")
    void shouldThrowExceptionWhenSettingInvalidYearViaSetter() {
        VehicleReleaseYear vehicleReleaseYear = new VehicleReleaseYear();
        int invalidYear = 1800;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vehicleReleaseYear.setValue(invalidYear));

        assertEquals("Vehicle release year must be after 1900. Provided: " + invalidYear,
                exception.getMessage());
    }

    @Test
    @DisplayName("Should set valid year via setter")
    void shouldSetValidYearViaSetter() {
        VehicleReleaseYear vehicleReleaseYear = new VehicleReleaseYear();
        int validYear = 2015;

        vehicleReleaseYear.setValue(validYear);

        assertEquals(validYear, vehicleReleaseYear.getValue());
    }

    @Test
    @DisplayName("Should handle boundary years correctly")
    void shouldHandleBoundaryYearsCorrectly() {
        int currentYear = java.time.Year.now().getValue();

        VehicleReleaseYear year1900 = new VehicleReleaseYear(1900);
        assertEquals(1900, year1900.getValue());

        VehicleReleaseYear currentYearObj = new VehicleReleaseYear(currentYear);

        assertEquals(currentYear, currentYearObj.getValue());
        assertThrows(IllegalArgumentException.class, () -> new VehicleReleaseYear(1899));
        assertThrows(IllegalArgumentException.class, () -> new VehicleReleaseYear(currentYear + 1));
    }
}