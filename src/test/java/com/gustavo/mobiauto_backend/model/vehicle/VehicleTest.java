package com.gustavo.mobiauto_backend.model.vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Vehicle Tests")
class VehicleTest {

    private Vehicle vehicle;
    private VehicleType type;
    private VehicleModel model;
    private VehicleReleaseYear releaseYear;
    private VehicleColor color;

    @BeforeEach
    void setUp() {
        type = VehicleType.CAR;
        model = new VehicleModel("Toyota Corolla");
        releaseYear = new VehicleReleaseYear(2020);
        color = new VehicleColor("Blue");
    }

    @Test
    @DisplayName("Should create vehicle with all parameters")
    void shouldCreateVehicleWithAllParameters() {
        vehicle = new Vehicle(type, model, releaseYear, color);

        assertNotNull(vehicle);
        assertEquals(type, vehicle.getType());
        assertEquals(model, vehicle.getModel());
        assertEquals(releaseYear, vehicle.getReleaseYear());
        assertEquals(color, vehicle.getColor());
        assertNull(vehicle.getId());
    }

    @Test
    @DisplayName("Should create vehicle using no-args constructor")
    void shouldCreateVehicleUsingNoArgsConstructor() {
        vehicle = new Vehicle();

        assertNotNull(vehicle);
        assertNull(vehicle.getType());
        assertNull(vehicle.getModel());
        assertNull(vehicle.getReleaseYear());
        assertNull(vehicle.getColor());
        assertNull(vehicle.getId());
    }

    @Test
    @DisplayName("Should create vehicle with ID using all-args constructor")
    void shouldCreateVehicleWithIdUsingAllArgsConstructor() {
        Long id = 1L;
        vehicle = new Vehicle(id, type, model, releaseYear, color);

        assertNotNull(vehicle);
        assertEquals(id, vehicle.getId());
        assertEquals(type, vehicle.getType());
        assertEquals(model, vehicle.getModel());
        assertEquals(releaseYear, vehicle.getReleaseYear());
        assertEquals(color, vehicle.getColor());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllPropertiesCorrectly() {
        vehicle = new Vehicle();
        Long id = 1L;
        VehicleType newType = VehicleType.MOTORCYCLE;
        VehicleModel newModel = new VehicleModel("Honda CBR");
        VehicleReleaseYear newReleaseYear = new VehicleReleaseYear(2021);
        VehicleColor newColor = new VehicleColor("Red");

        vehicle.setId(id);
        vehicle.setType(newType);
        vehicle.setModel(newModel);
        vehicle.setReleaseYear(newReleaseYear);
        vehicle.setColor(newColor);

        assertEquals(id, vehicle.getId());
        assertEquals(newType, vehicle.getType());
        assertEquals(newModel, vehicle.getModel());
        assertEquals(newReleaseYear, vehicle.getReleaseYear());
        assertEquals(newColor, vehicle.getColor());
    }

    @Test
    @DisplayName("Should throw exception when setting null type")
    void shouldThrowExceptionWhenSettingNullType() {
        vehicle = new Vehicle();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vehicle.setType(null));

        assertEquals("Vehicle type cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting null model")
    void shouldThrowExceptionWhenSettingNullModel() {
        vehicle = new Vehicle();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vehicle.setModel(null));

        assertEquals("Vehicle model cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting null release year")
    void shouldThrowExceptionWhenSettingNullReleaseYear() {
        vehicle = new Vehicle();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vehicle.setReleaseYear(null));

        assertEquals("Vehicle release year cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting null color")
    void shouldThrowExceptionWhenSettingNullColor() {
        vehicle = new Vehicle();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> vehicle.setColor(null));

        assertEquals("Vehicle color cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating vehicle with null type")
    void shouldThrowExceptionWhenCreatingVehicleWithNullType() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Vehicle(null, model, releaseYear, color));

        assertEquals("Vehicle type cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating vehicle with null model")
    void shouldThrowExceptionWhenCreatingVehicleWithNullModel() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Vehicle(type, null, releaseYear, color));

        assertEquals("Vehicle model cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating vehicle with null release year")
    void shouldThrowExceptionWhenCreatingVehicleWithNullReleaseYear() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Vehicle(type, model, null, color));

        assertEquals("Vehicle release year cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating vehicle with null color")
    void shouldThrowExceptionWhenCreatingVehicleWithNullColor() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Vehicle(type, model, releaseYear, null));

        assertEquals("Vehicle color cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle different vehicle types")
    void shouldHandleDifferentVehicleTypes() {
        vehicle = new Vehicle(VehicleType.CAR, model, releaseYear, color);
        assertEquals(VehicleType.CAR, vehicle.getType());

        vehicle.setType(VehicleType.MOTORCYCLE);
        assertEquals(VehicleType.MOTORCYCLE, vehicle.getType());

        vehicle.setType(VehicleType.TRUCK);
        assertEquals(VehicleType.TRUCK, vehicle.getType());

        vehicle.setType(VehicleType.OTHER);
        assertEquals(VehicleType.OTHER, vehicle.getType());
    }
}