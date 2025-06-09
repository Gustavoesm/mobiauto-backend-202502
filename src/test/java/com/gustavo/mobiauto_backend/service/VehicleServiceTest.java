package com.gustavo.mobiauto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gustavo.mobiauto_backend.controller.requests.VehicleRequest;
import com.gustavo.mobiauto_backend.infra.exceptions.VehicleNotFoundException;
import com.gustavo.mobiauto_backend.infra.repositories.VehicleRepository;
import com.gustavo.mobiauto_backend.model.vehicle.Vehicle;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleColor;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleModel;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleReleaseYear;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleType;

@ExtendWith(MockitoExtension.class)
@DisplayName("VehicleService Tests")
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private VehicleRequest vehicleRequest;
    private Vehicle savedVehicle;
    private Vehicle existingVehicle;

    @BeforeEach
    void setUp() {
        vehicleRequest = new VehicleRequest();
        vehicleRequest.setType("CAR");
        vehicleRequest.setModel("Toyota Corolla");
        vehicleRequest.setReleaseYear(2023);
        vehicleRequest.setColor("Blue");

        savedVehicle = new Vehicle(
                VehicleType.CAR,
                new VehicleModel("Toyota Corolla"),
                new VehicleReleaseYear(2023),
                new VehicleColor("Blue"));
        savedVehicle.setId(1L);

        existingVehicle = new Vehicle(
                VehicleType.MOTORCYCLE,
                new VehicleModel("Honda CBR"),
                new VehicleReleaseYear(2020),
                new VehicleColor("Red"));
        existingVehicle.setId(1L);
    }

    @Test
    @DisplayName("Should create CAR type vehicle")
    void shouldCreateCarTypeVehicle() {
        vehicleRequest.setType("CAR");
        Vehicle carVehicle = new Vehicle(VehicleType.CAR, new VehicleModel("Toyota Camry"),
                new VehicleReleaseYear(2023), new VehicleColor("White"));
        carVehicle.setId(1L);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(carVehicle);

        Vehicle result = vehicleService.createVehicle(vehicleRequest);

        assertNotNull(result);
        assertEquals(VehicleType.CAR, result.getType());
        assertEquals("Toyota Camry", result.getModel().getValue());
        assertEquals(2023, result.getReleaseYear().getValue());
        assertEquals("White", result.getColor().getValue());
        assertEquals(1L, result.getId());

        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should create MOTORCYCLE type vehicle")
    void shouldCreateMotorcycleTypeVehicle() {
        vehicleRequest.setType("MOTORCYCLE");
        Vehicle motorcycleVehicle = new Vehicle(VehicleType.MOTORCYCLE, new VehicleModel("Honda CBR"),
                new VehicleReleaseYear(2023), new VehicleColor("Black"));
        motorcycleVehicle.setId(2L);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(motorcycleVehicle);

        Vehicle result = vehicleService.createVehicle(vehicleRequest);

        assertNotNull(result);
        assertEquals(VehicleType.MOTORCYCLE, result.getType());
        assertEquals("Honda CBR", result.getModel().getValue());
        assertEquals(2023, result.getReleaseYear().getValue());
        assertEquals("Black", result.getColor().getValue());
        assertEquals(2L, result.getId());

        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should create TRUCK type vehicle")
    void shouldCreateTruckTypeVehicle() {
        vehicleRequest.setType("TRUCK");
        Vehicle truckVehicle = new Vehicle(VehicleType.TRUCK, new VehicleModel("Ford F-150"),
                new VehicleReleaseYear(2023), new VehicleColor("Red"));
        truckVehicle.setId(3L);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(truckVehicle);

        Vehicle result = vehicleService.createVehicle(vehicleRequest);

        assertNotNull(result);
        assertEquals(VehicleType.TRUCK, result.getType());
        assertEquals("Ford F-150", result.getModel().getValue());
        assertEquals(2023, result.getReleaseYear().getValue());
        assertEquals("Red", result.getColor().getValue());
        assertEquals(3L, result.getId());

        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should handle lowercase vehicle type in create")
    void shouldHandleLowercaseVehicleTypeInCreate() {
        vehicleRequest.setType("car"); // lowercase
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(savedVehicle);

        Vehicle result = vehicleService.createVehicle(vehicleRequest);

        assertEquals(VehicleType.CAR, result.getType());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should throw exception when creating vehicle with invalid type")
    void shouldThrowExceptionWhenCreatingVehicleWithInvalidType() {
        vehicleRequest.setType("INVALID_TYPE");

        assertThrows(IllegalArgumentException.class, () -> {
            vehicleService.createVehicle(vehicleRequest);
        });

        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should get vehicle by id successfully")
    void shouldGetVehicleByIdSuccessfully() {
        Long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));

        Vehicle result = vehicleService.getVehicle(vehicleId);

        assertNotNull(result);
        assertEquals(vehicleId, result.getId());
        assertEquals(VehicleType.MOTORCYCLE, result.getType());
        assertEquals("Honda CBR", result.getModel().getValue());
        assertEquals(2020, result.getReleaseYear().getValue());
        assertEquals("Red", result.getColor().getValue());

        verify(vehicleRepository, times(1)).findById(vehicleId);
    }

    @Test
    @DisplayName("Should throw VehicleNotFoundException when vehicle not found")
    void shouldThrowVehicleNotFoundExceptionWhenVehicleNotFound() {
        Long nonExistentId = 999L;
        when(vehicleRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> {
            vehicleService.getVehicle(nonExistentId);
        });

        assertNotNull(exception);
        assertEquals("Vehicle with id " + nonExistentId + " not found.", exception.getMessage());
        verify(vehicleRepository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should update vehicle with all fields")
    void shouldUpdateVehicleWithAllFields() {
        Long vehicleId = 1L;
        VehicleRequest updateRequest = new VehicleRequest();
        updateRequest.setType("CAR");
        updateRequest.setModel("Toyota Prius");
        updateRequest.setReleaseYear(2024);
        updateRequest.setColor("Green");

        Vehicle updatedVehicle = new Vehicle(
                VehicleType.CAR,
                new VehicleModel("Toyota Prius"),
                new VehicleReleaseYear(2024),
                new VehicleColor("Green"));
        updatedVehicle.setId(vehicleId);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(updatedVehicle);

        Vehicle result = vehicleService.updateVehicle(vehicleId, updateRequest);

        assertNotNull(result);
        assertEquals(vehicleId, result.getId());
        assertEquals(VehicleType.CAR, result.getType());
        assertEquals("Toyota Prius", result.getModel().getValue());
        assertEquals(2024, result.getReleaseYear().getValue());
        assertEquals("Green", result.getColor().getValue());

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(existingVehicle);
    }

    @Test
    @DisplayName("Should update vehicle with partial fields")
    void shouldUpdateVehicleWithPartialFields() {
        Long vehicleId = 1L;
        VehicleRequest partialUpdateRequest = new VehicleRequest();
        partialUpdateRequest.setModel("Honda CBR 1000RR");
        partialUpdateRequest.setColor("Black");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(existingVehicle);

        Vehicle result = vehicleService.updateVehicle(vehicleId, partialUpdateRequest);

        assertNotNull(result);
        assertEquals(vehicleId, result.getId());
        assertEquals(VehicleType.MOTORCYCLE, result.getType());
        assertEquals(2020, result.getReleaseYear().getValue());

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(existingVehicle);
    }

    @Test
    @DisplayName("Should update vehicle with only type")
    void shouldUpdateVehicleWithOnlyType() {
        Long vehicleId = 1L;
        VehicleRequest typeUpdateRequest = new VehicleRequest();
        typeUpdateRequest.setType("TRUCK");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(existingVehicle);

        vehicleService.updateVehicle(vehicleId, typeUpdateRequest);

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(existingVehicle);
        assertEquals(VehicleType.TRUCK, existingVehicle.getType());
    }

    @Test
    @DisplayName("Should update vehicle with only model")
    void shouldUpdateVehicleWithOnlyModel() {
        Long vehicleId = 1L;
        VehicleRequest modelUpdateRequest = new VehicleRequest();
        modelUpdateRequest.setModel("Yamaha R1");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(existingVehicle);

        vehicleService.updateVehicle(vehicleId, modelUpdateRequest);

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(existingVehicle);
        assertEquals("Yamaha R1", existingVehicle.getModel().getValue());
    }

    @Test
    @DisplayName("Should update vehicle with only release year")
    void shouldUpdateVehicleWithOnlyReleaseYear() {
        Long vehicleId = 1L;
        VehicleRequest yearUpdateRequest = new VehicleRequest();
        yearUpdateRequest.setReleaseYear(2025);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(existingVehicle);

        vehicleService.updateVehicle(vehicleId, yearUpdateRequest);

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(existingVehicle);
        assertEquals(2025, existingVehicle.getReleaseYear().getValue());
    }

    @Test
    @DisplayName("Should update vehicle with only color")
    void shouldUpdateVehicleWithOnlyColor() {
        Long vehicleId = 1L;
        VehicleRequest colorUpdateRequest = new VehicleRequest();
        colorUpdateRequest.setColor("Purple");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(existingVehicle);

        vehicleService.updateVehicle(vehicleId, colorUpdateRequest);

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(existingVehicle);
        assertEquals("Purple", existingVehicle.getColor().getValue());
    }

    @Test
    @DisplayName("Should handle lowercase vehicle type in update")
    void shouldHandleLowercaseVehicleTypeInUpdate() {
        Long vehicleId = 1L;
        VehicleRequest updateRequest = new VehicleRequest();
        updateRequest.setType("car");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(existingVehicle);

        vehicleService.updateVehicle(vehicleId, updateRequest);

        assertEquals(VehicleType.CAR, existingVehicle.getType());
        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(existingVehicle);
    }

    @Test
    @DisplayName("Should throw exception when updating vehicle with invalid type")
    void shouldThrowExceptionWhenUpdatingVehicleWithInvalidType() {
        Long vehicleId = 1L;
        VehicleRequest updateRequest = new VehicleRequest();
        updateRequest.setType("INVALID_TYPE");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));

        assertThrows(IllegalArgumentException.class, () -> {
            vehicleService.updateVehicle(vehicleId, updateRequest);
        });

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should throw VehicleNotFoundException when updating non-existent vehicle")
    void shouldThrowVehicleNotFoundExceptionWhenUpdatingNonExistentVehicle() {
        Long nonExistentId = 999L;
        VehicleRequest updateRequest = new VehicleRequest();
        updateRequest.setType("CAR");

        when(vehicleRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> {
            vehicleService.updateVehicle(nonExistentId, updateRequest);
        });

        assertNotNull(exception);
        assertEquals("Vehicle with id " + nonExistentId + " not found.", exception.getMessage());
        verify(vehicleRepository, times(1)).findById(nonExistentId);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should not update vehicle when all fields are null")
    void shouldNotUpdateVehicleWhenAllFieldsAreNull() {
        Long vehicleId = 1L;
        VehicleRequest emptyUpdateRequest = new VehicleRequest();

        Vehicle originalVehicle = new Vehicle(
                VehicleType.MOTORCYCLE,
                new VehicleModel("Honda CBR"),
                new VehicleReleaseYear(2020),
                new VehicleColor("Red"));
        originalVehicle.setId(vehicleId);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(originalVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(originalVehicle);

        Vehicle result = vehicleService.updateVehicle(vehicleId, emptyUpdateRequest);

        assertNotNull(result);
        assertEquals(VehicleType.MOTORCYCLE, result.getType());
        assertEquals("Honda CBR", result.getModel().getValue());
        assertEquals(2020, result.getReleaseYear().getValue());
        assertEquals("Red", result.getColor().getValue());

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(originalVehicle);
    }
}