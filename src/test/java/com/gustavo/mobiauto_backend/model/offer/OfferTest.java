package com.gustavo.mobiauto_backend.model.offer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.gustavo.mobiauto_backend.model.store.Store;
import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.model.vehicle.Vehicle;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleColor;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleModel;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleReleaseYear;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleType;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Offer Tests")
class OfferTest {

    private Offer offer;
    private User client;
    private Vehicle vehicle;
    private Store store;

    @BeforeEach
    void setUp() {
        client = new User("John", "Doe", "john.doe@example.com", "password123");
        client.setId(1L);

        vehicle = new Vehicle(VehicleType.CAR,
                new VehicleModel("Toyota Corolla"),
                new VehicleReleaseYear(2023),
                new VehicleColor("Red"));
        vehicle.setId(1L);

        store = new Store("Car Store ABC", "25.215.908/0001-43");
    }

    @Test
    @DisplayName("Should create offer with all parameters and specific status")
    void shouldCreateOfferWithAllParametersAndSpecificStatus() {
        offer = new Offer(OfferStatus.ATTENDED, client, vehicle, store);

        assertNotNull(offer);
        assertEquals(OfferStatus.ATTENDED, offer.getStatus());
        assertEquals(client, offer.getClient());
        assertEquals(vehicle, offer.getVehicle());
        assertEquals(store, offer.getStore());
        assertTrue(offer.isActive());
        assertNull(offer.getId());
    }

    @Test
    @DisplayName("Should create offer with client, vehicle and store (default NEW status)")
    void shouldCreateOfferWithClientVehicleAndStore() {
        offer = new Offer(client, vehicle, store);

        assertNotNull(offer);
        assertEquals(OfferStatus.NEW, offer.getStatus());
        assertEquals(client, offer.getClient());
        assertEquals(vehicle, offer.getVehicle());
        assertEquals(store, offer.getStore());
        assertTrue(offer.isActive());
        assertNull(offer.getId());
    }

    @Test
    @DisplayName("Should create offer using no-args constructor")
    void shouldCreateOfferUsingNoArgsConstructor() {
        offer = new Offer();

        assertNotNull(offer);
        assertNull(offer.getStatus());
        assertNull(offer.getClient());
        assertNull(offer.getVehicle());
        assertNull(offer.getStore());
        assertFalse(offer.isActive());
        assertNull(offer.getId());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllPropertiesCorrectly() {
        offer = new Offer();
        Long id = 1L;

        offer.setId(id);
        offer.setStatus(OfferStatus.COMPLETED);
        offer.setClient(client);
        offer.setVehicle(vehicle);
        offer.setStore(store);
        offer.setActive(true);

        assertEquals(id, offer.getId());
        assertEquals(OfferStatus.COMPLETED, offer.getStatus());
        assertEquals(client, offer.getClient());
        assertEquals(vehicle, offer.getVehicle());
        assertEquals(store, offer.getStore());
        assertTrue(offer.isActive());
    }

    @Test
    @DisplayName("Should handle offer activation and deactivation")
    void shouldHandleOfferActivationAndDeactivation() {
        offer = new Offer(client, vehicle, store);

        assertTrue(offer.isActive());

        offer.setActive(false);
        assertFalse(offer.isActive());

        offer.setActive(true);
        assertTrue(offer.isActive());
    }

    @Test
    @DisplayName("Should handle status changes")
    void shouldHandleStatusChanges() {
        offer = new Offer(client, vehicle, store);

        assertEquals(OfferStatus.NEW, offer.getStatus());

        offer.setStatus(OfferStatus.ATTENDED);
        assertEquals(OfferStatus.ATTENDED, offer.getStatus());

        offer.setStatus(OfferStatus.COMPLETED);
        assertEquals(OfferStatus.COMPLETED, offer.getStatus());
    }

    @Test
    @DisplayName("Should handle client updates")
    void shouldHandleClientUpdates() {
        offer = new Offer(client, vehicle, store);
        User newClient = new User("Jane", "Smith", "jane.smith@example.com", "password456");
        newClient.setId(2L);

        offer.setClient(newClient);

        assertEquals(newClient, offer.getClient());
        assertEquals("jane.smith@example.com", offer.getClient().getEmail().getValue());
    }

    @Test
    @DisplayName("Should handle vehicle updates")
    void shouldHandleVehicleUpdates() {
        offer = new Offer(client, vehicle, store);
        Vehicle newVehicle = new Vehicle(VehicleType.CAR,
                new VehicleModel("Honda Civic"),
                new VehicleReleaseYear(2024),
                new VehicleColor("Blue"));
        newVehicle.setId(2L);

        offer.setVehicle(newVehicle);

        assertEquals(newVehicle, offer.getVehicle());
        assertEquals("Honda Civic", offer.getVehicle().getModel().getValue());
    }

    @Test
    @DisplayName("Should handle store updates")
    void shouldHandleStoreUpdates() {
        offer = new Offer(client, vehicle, store);
        Store newStore = new Store("New Car Store XYZ", "11.222.333/0001-81");

        offer.setStore(newStore);

        assertEquals(newStore, offer.getStore());
        assertEquals("New Car Store XYZ", offer.getStore().getCompanyName().getValue());
    }

    @Test
    @DisplayName("Should maintain relationships integrity")
    void shouldMaintainRelationshipsIntegrity() {
        offer = new Offer(OfferStatus.ATTENDED, client, vehicle, store);

        assertNotNull(offer.getClient());
        assertNotNull(offer.getVehicle());
        assertNotNull(offer.getStore());

        assertEquals(client.getId(), offer.getClient().getId());
        assertEquals(vehicle.getId(), offer.getVehicle().getId());
    }

    @Test
    @DisplayName("Should handle id assignment")
    void shouldHandleIdAssignment() {
        offer = new Offer(client, vehicle, store);
        Long offerId = 100L;

        assertNull(offer.getId());

        offer.setId(offerId);

        assertEquals(offerId, offer.getId());
    }

    @Test
    @DisplayName("Should create offer with different status values")
    void shouldCreateOfferWithDifferentStatusValues() {
        Offer newOffer = new Offer(OfferStatus.NEW, client, vehicle, store);
        Offer attendedOffer = new Offer(OfferStatus.ATTENDED, client, vehicle, store);
        Offer completedOffer = new Offer(OfferStatus.COMPLETED, client, vehicle, store);

        assertEquals(OfferStatus.NEW, newOffer.getStatus());
        assertEquals(OfferStatus.ATTENDED, attendedOffer.getStatus());
        assertEquals(OfferStatus.COMPLETED, completedOffer.getStatus());

        assertTrue(newOffer.isActive());
        assertTrue(attendedOffer.isActive());
        assertTrue(completedOffer.isActive());
    }

    @Test
    @DisplayName("Should allow null values for optional fields when using setters")
    void shouldAllowNullValuesForOptionalFieldsWhenUsingSetters() {
        offer = new Offer();

        offer.setStatus(null);
        offer.setClient(null);
        offer.setVehicle(null);
        offer.setStore(null);

        assertNull(offer.getStatus());
        assertNull(offer.getClient());
        assertNull(offer.getVehicle());
        assertNull(offer.getStore());
    }
}