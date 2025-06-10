package com.gustavo.mobiauto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gustavo.mobiauto_backend.controller.requests.VehicleRequest;
import com.gustavo.mobiauto_backend.infra.exceptions.OfferNotFoundException;
import com.gustavo.mobiauto_backend.infra.repositories.OfferRepository;
import com.gustavo.mobiauto_backend.model.offer.Offer;
import com.gustavo.mobiauto_backend.model.offer.OfferStatus;
import com.gustavo.mobiauto_backend.model.store.Store;
import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.model.vehicle.Vehicle;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleColor;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleModel;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleReleaseYear;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleType;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyActiveException;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyDeactivatedException;
import com.gustavo.mobiauto_backend.service.exceptions.DeactivatedOfferException;

@ExtendWith(MockitoExtension.class)
@DisplayName("OfferService Tests")
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private UserService userService;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private OfferService offerService;

    private VehicleRequest validVehicleRequest;
    private Store testStore;
    private User testUser;
    private Vehicle testVehicle;
    private Offer testOffer;
    private Vehicle updatedVehicle;

    private static final Long OFFER_ID = 1L;
    private static final Long STORE_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long VEHICLE_ID = 1L;

    @BeforeEach
    void setUp() throws Exception {
        validVehicleRequest = new VehicleRequest();
        validVehicleRequest.setType("CAR");
        validVehicleRequest.setModel("Toyota Corolla");
        validVehicleRequest.setReleaseYear(2023);
        validVehicleRequest.setColor("Blue");

        testStore = new Store("Test Store", "11.222.333/0001-81");
        setStoreId(testStore, STORE_ID);

        testUser = new User("John", "Doe", "john.doe@example.com", "password123");
        setUserId(testUser, USER_ID);

        testVehicle = new Vehicle(
                VehicleType.CAR,
                new VehicleModel("Toyota Corolla"),
                new VehicleReleaseYear(2023),
                new VehicleColor("Blue"));
        testVehicle.setId(VEHICLE_ID);

        updatedVehicle = new Vehicle(
                VehicleType.CAR,
                new VehicleModel("Honda Civic"),
                new VehicleReleaseYear(2024),
                new VehicleColor("Red"));
        updatedVehicle.setId(VEHICLE_ID);

        testOffer = new Offer(testUser, testVehicle, testStore);
        setOfferId(testOffer, OFFER_ID);
    }

    @Test
    @DisplayName("Should register offer successfully")
    void shouldRegisterOfferSuccessfully() {
        when(storeService.getStore(STORE_ID)).thenReturn(testStore);
        when(userService.findUser(USER_ID)).thenReturn(testUser);
        when(vehicleService.createVehicle(validVehicleRequest)).thenReturn(testVehicle);
        when(offerRepository.save(any(Offer.class))).thenReturn(testOffer);

        Offer result = offerService.registerOffer(STORE_ID, USER_ID, validVehicleRequest);

        assertNotNull(result);
        assertEquals(OFFER_ID, result.getId());
        assertEquals(testUser, result.getClient());
        assertEquals(testVehicle, result.getVehicle());
        assertEquals(testStore, result.getStore());
        assertEquals(OfferStatus.NEW, result.getStatus());
        assertTrue(result.isActive());

        verify(storeService).getStore(STORE_ID);
        verify(userService).findUser(USER_ID);
        verify(vehicleService).createVehicle(validVehicleRequest);
        verify(offerRepository).save(any(Offer.class));
    }

    @Test
    @DisplayName("Should get offer successfully")
    void shouldGetOfferSuccessfully() {
        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));

        Offer result = offerService.getOffer(OFFER_ID);

        assertNotNull(result);
        assertEquals(OFFER_ID, result.getId());
        assertEquals(testUser, result.getClient());
        assertEquals(testVehicle, result.getVehicle());
        assertEquals(testStore, result.getStore());

        verify(offerRepository).findById(OFFER_ID);
    }

    @Test
    @DisplayName("Should throw OfferNotFoundException when offer does not exist")
    void shouldThrowOfferNotFoundExceptionWhenOfferDoesNotExist() {
        Long nonExistentId = 999L;
        when(offerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        OfferNotFoundException exception = assertThrows(
                OfferNotFoundException.class,
                () -> offerService.getOffer(nonExistentId));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Offer with id " + nonExistentId + " not found"));
        verify(offerRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should deactivate offer successfully")
    void shouldDeactivateOfferSuccessfully() {
        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));
        when(offerRepository.save(any(Offer.class))).thenReturn(testOffer);

        Offer result = offerService.deactivateOffer(OFFER_ID);

        assertNotNull(result);
        assertFalse(result.isActive());

        verify(offerRepository).findById(OFFER_ID);
        verify(offerRepository).save(testOffer);
    }

    @Test
    @DisplayName("Should throw AlreadyDeactivatedException when offer is already inactive")
    void shouldThrowAlreadyDeactivatedExceptionWhenOfferIsAlreadyInactive() {
        testOffer.setActive(false);
        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));

        AlreadyDeactivatedException exception = assertThrows(
                AlreadyDeactivatedException.class,
                () -> offerService.deactivateOffer(OFFER_ID));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Offer with ID " + OFFER_ID + " is already deactivated"));

        verify(offerRepository).findById(OFFER_ID);
        verify(offerRepository, never()).save(any(Offer.class));
    }

    @Test
    @DisplayName("Should throw OfferNotFoundException when deactivating non-existent offer")
    void shouldThrowOfferNotFoundExceptionWhenDeactivatingNonExistentOffer() {
        Long nonExistentId = 999L;
        when(offerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        OfferNotFoundException exception = assertThrows(
                OfferNotFoundException.class,
                () -> offerService.deactivateOffer(nonExistentId));

        assertNotNull(exception);
        verify(offerRepository).findById(nonExistentId);
        verify(offerRepository, never()).save(any(Offer.class));
    }

    @Test
    @DisplayName("Should update offer successfully")
    void shouldUpdateOfferSuccessfully() {
        VehicleRequest updateRequest = new VehicleRequest();
        updateRequest.setModel("Honda Civic");
        updateRequest.setColor("Red");

        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));
        when(vehicleService.updateVehicle(VEHICLE_ID, updateRequest)).thenReturn(updatedVehicle);
        when(offerRepository.save(any(Offer.class))).thenReturn(testOffer);

        Offer result = offerService.updateOffer(OFFER_ID, updateRequest);

        assertNotNull(result);
        assertEquals(updatedVehicle, result.getVehicle());

        verify(offerRepository).findById(OFFER_ID);
        verify(vehicleService).updateVehicle(VEHICLE_ID, updateRequest);
        verify(offerRepository).save(testOffer);
    }

    @Test
    @DisplayName("Should throw DeactivatedOfferException when updating inactive offer")
    void shouldThrowDeactivatedOfferExceptionWhenUpdatingInactiveOffer() {
        testOffer.setActive(false);
        VehicleRequest updateRequest = new VehicleRequest();
        updateRequest.setModel("Honda Civic");

        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));

        DeactivatedOfferException exception = assertThrows(
                DeactivatedOfferException.class,
                () -> offerService.updateOffer(OFFER_ID, updateRequest));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Offer with ID " + OFFER_ID + " is deactivated"));

        verify(offerRepository).findById(OFFER_ID);
        verify(vehicleService, never()).updateVehicle(any(Long.class), any(VehicleRequest.class));
        verify(offerRepository, never()).save(any(Offer.class));
    }

    @Test
    @DisplayName("Should throw OfferNotFoundException when updating non-existent offer")
    void shouldThrowOfferNotFoundExceptionWhenUpdatingNonExistentOffer() {
        Long nonExistentId = 999L;
        VehicleRequest updateRequest = new VehicleRequest();
        updateRequest.setModel("Honda Civic");

        when(offerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        OfferNotFoundException exception = assertThrows(
                OfferNotFoundException.class,
                () -> offerService.updateOffer(nonExistentId, updateRequest));

        assertNotNull(exception);
        verify(offerRepository).findById(nonExistentId);
        verify(vehicleService, never()).updateVehicle(any(Long.class), any(VehicleRequest.class));
        verify(offerRepository, never()).save(any(Offer.class));
    }

    @Test
    @DisplayName("Should reactivate offer successfully")
    void shouldReactivateOfferSuccessfully() {
        testOffer.setActive(false);
        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));
        when(offerRepository.save(any(Offer.class))).thenReturn(testOffer);

        Offer result = offerService.reactivateOffer(OFFER_ID);

        assertNotNull(result);
        assertTrue(result.isActive());

        verify(offerRepository).findById(OFFER_ID);
        verify(offerRepository).save(testOffer);
    }

    @Test
    @DisplayName("Should throw AlreadyActiveException when offer is already active")
    void shouldThrowAlreadyActiveExceptionWhenOfferIsAlreadyActive() {
        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));

        AlreadyActiveException exception = assertThrows(
                AlreadyActiveException.class,
                () -> offerService.reactivateOffer(OFFER_ID));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Offer with ID " + OFFER_ID + " is already active"));

        verify(offerRepository).findById(OFFER_ID);
        verify(offerRepository, never()).save(any(Offer.class));
    }

    @Test
    @DisplayName("Should throw OfferNotFoundException when reactivating non-existent offer")
    void shouldThrowOfferNotFoundExceptionWhenReactivatingNonExistentOffer() {
        Long nonExistentId = 999L;
        when(offerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        OfferNotFoundException exception = assertThrows(
                OfferNotFoundException.class,
                () -> offerService.reactivateOffer(nonExistentId));

        assertNotNull(exception);
        verify(offerRepository).findById(nonExistentId);
        verify(offerRepository, never()).save(any(Offer.class));
    }

    @Test
    @DisplayName("Should handle complete offer lifecycle")
    void shouldHandleCompleteOfferLifecycle() {
        when(storeService.getStore(STORE_ID)).thenReturn(testStore);
        when(userService.findUser(USER_ID)).thenReturn(testUser);
        when(vehicleService.createVehicle(validVehicleRequest)).thenReturn(testVehicle);
        when(offerRepository.save(any(Offer.class))).thenReturn(testOffer);

        Offer registeredOffer = offerService.registerOffer(STORE_ID, USER_ID, validVehicleRequest);
        assertTrue(registeredOffer.isActive());

        VehicleRequest updateRequest = new VehicleRequest();
        updateRequest.setColor("Green");
        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));
        when(vehicleService.updateVehicle(eq(VEHICLE_ID), any(VehicleRequest.class))).thenReturn(updatedVehicle);

        offerService.updateOffer(OFFER_ID, updateRequest);

        offerService.deactivateOffer(OFFER_ID);

        testOffer.setActive(false);
        offerService.reactivateOffer(OFFER_ID);

        verify(storeService).getStore(STORE_ID);
        verify(userService).findUser(USER_ID);
        verify(vehicleService).createVehicle(validVehicleRequest);
        verify(vehicleService).updateVehicle(eq(VEHICLE_ID), any(VehicleRequest.class));
        verify(offerRepository, atLeast(3)).findById(OFFER_ID);
        verify(offerRepository, atLeast(4)).save(any(Offer.class));
    }

    @Test
    @DisplayName("Should handle offer with different vehicle types")
    void shouldHandleOfferWithDifferentVehicleTypes() {
        VehicleRequest motorcycleRequest = new VehicleRequest();
        motorcycleRequest.setType("MOTORCYCLE");
        motorcycleRequest.setModel("Honda CBR");
        motorcycleRequest.setReleaseYear(2023);
        motorcycleRequest.setColor("Black");

        Vehicle motorcycleVehicle = new Vehicle(
                VehicleType.MOTORCYCLE,
                new VehicleModel("Honda CBR"),
                new VehicleReleaseYear(2023),
                new VehicleColor("Black"));
        motorcycleVehicle.setId(VEHICLE_ID);

        when(storeService.getStore(STORE_ID)).thenReturn(testStore);
        when(userService.findUser(USER_ID)).thenReturn(testUser);
        when(vehicleService.createVehicle(motorcycleRequest)).thenReturn(motorcycleVehicle);
        when(offerRepository.save(any(Offer.class))).thenReturn(testOffer);

        Offer result = offerService.registerOffer(STORE_ID, USER_ID, motorcycleRequest);

        assertNotNull(result);
        verify(vehicleService).createVehicle(motorcycleRequest);
    }

    @Test
    @DisplayName("Should handle offer with different offer statuses")
    void shouldHandleOfferWithDifferentOfferStatuses() throws Exception {
        Offer attendedOffer = new Offer(OfferStatus.ATTENDED, testUser, testVehicle, testStore);
        setOfferId(attendedOffer, OFFER_ID);

        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(attendedOffer));

        Offer result = offerService.getOffer(OFFER_ID);

        assertNotNull(result);
        assertEquals(OfferStatus.ATTENDED, result.getStatus());
    }

    @Test
    @DisplayName("Should maintain offer relationships after operations")
    void shouldMaintainOfferRelationshipsAfterOperations() {
        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));
        when(offerRepository.save(any(Offer.class))).thenReturn(testOffer);

        Offer result = offerService.deactivateOffer(OFFER_ID);

        assertNotNull(result);
        assertEquals(testUser, result.getClient());
        assertEquals(testVehicle, result.getVehicle());
        assertEquals(testStore, result.getStore());
        assertEquals(OFFER_ID, result.getId());
    }

    @Test
    @DisplayName("Should verify repository interactions for successful operations")
    void shouldVerifyRepositoryInteractionsForSuccessfulOperations() {
        when(storeService.getStore(STORE_ID)).thenReturn(testStore);
        when(userService.findUser(USER_ID)).thenReturn(testUser);
        when(vehicleService.createVehicle(validVehicleRequest)).thenReturn(testVehicle);
        when(offerRepository.save(any(Offer.class))).thenReturn(testOffer);
        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));

        offerService.registerOffer(STORE_ID, USER_ID, validVehicleRequest);

        offerService.getOffer(OFFER_ID);

        offerService.deactivateOffer(OFFER_ID);

        verify(storeService).getStore(STORE_ID);
        verify(userService).findUser(USER_ID);
        verify(vehicleService).createVehicle(validVehicleRequest);
        verify(offerRepository, times(2)).save(any(Offer.class));
        verify(offerRepository, times(2)).findById(OFFER_ID);
    }

    @Test
    @DisplayName("Should handle edge case operations")
    void shouldHandleEdgeCaseOperations() {
        VehicleRequest minimalUpdate = new VehicleRequest();
        minimalUpdate.setColor("White");

        when(offerRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOffer));
        when(vehicleService.updateVehicle(VEHICLE_ID, minimalUpdate)).thenReturn(testVehicle);
        when(offerRepository.save(any(Offer.class))).thenReturn(testOffer);

        Offer result = offerService.updateOffer(OFFER_ID, minimalUpdate);

        assertNotNull(result);
        verify(vehicleService).updateVehicle(VEHICLE_ID, minimalUpdate);
    }

    private void setStoreId(Store store, Long id) throws Exception {
        Field idField = Store.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(store, id);
    }

    private void setUserId(User user, Long id) throws Exception {
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, id);
    }

    private void setOfferId(Offer offer, Long id) throws Exception {
        Field idField = Offer.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(offer, id);
    }
}