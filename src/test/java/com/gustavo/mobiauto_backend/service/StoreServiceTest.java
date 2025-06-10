package com.gustavo.mobiauto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gustavo.mobiauto_backend.controller.requests.StoreRequest;
import com.gustavo.mobiauto_backend.infra.exceptions.StoreNotFoundException;
import com.gustavo.mobiauto_backend.infra.repositories.OfferRepository;
import com.gustavo.mobiauto_backend.infra.repositories.StoreRepository;
import com.gustavo.mobiauto_backend.model.offer.Offer;
import com.gustavo.mobiauto_backend.model.store.Store;
import com.gustavo.mobiauto_backend.model.store.StoreName;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyActiveException;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyDeactivatedException;
import com.gustavo.mobiauto_backend.service.exceptions.DuplicateException;
import com.gustavo.mobiauto_backend.service.exceptions.EntityInUseException;

@ExtendWith(MockitoExtension.class)
@DisplayName("StoreService Tests")
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private StoreService storeService;

    private StoreRequest validStoreRequest;
    private Store testStore;
    private static final Long STORE_ID = 1L;
    private static final String STORE_NAME = "Test Store";
    private static final String STORE_CNPJ = "11.222.333/0001-81";

    @BeforeEach
    void setUp() throws Exception {
        validStoreRequest = new StoreRequest(STORE_NAME, STORE_CNPJ);
        testStore = new Store(STORE_NAME, STORE_CNPJ);

        Field idField = Store.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(testStore, STORE_ID);
    }

    @Test
    @DisplayName("Should register new store successfully")
    void shouldRegisterNewStoreSuccessfully() {
        when(storeRepository.findByCnpj(STORE_CNPJ)).thenReturn(Optional.empty());
        when(storeRepository.save(any(Store.class))).thenReturn(testStore);

        Store result = storeService.registerStore(validStoreRequest);

        assertNotNull(result);
        assertEquals(STORE_ID, result.getId());
        assertEquals(STORE_NAME, result.getCompanyName().getValue());
        assertEquals(STORE_CNPJ, result.getCnpj().getValue());
        assertTrue(result.isActive());

        verify(storeRepository).findByCnpj(STORE_CNPJ);
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    @DisplayName("Should throw DuplicateException when registering store with existing CNPJ")
    void shouldThrowDuplicateExceptionWhenRegisteringStoreWithExistingCnpj() {
        when(storeRepository.findByCnpj(STORE_CNPJ)).thenReturn(Optional.of(testStore));

        DuplicateException exception = assertThrows(
                DuplicateException.class,
                () -> storeService.registerStore(validStoreRequest));

        assertNotNull(exception);
        verify(storeRepository).findByCnpj(STORE_CNPJ);
        verify(storeRepository, never()).save(any(Store.class));
    }

    @Test
    @DisplayName("Should list only active stores")
    void shouldListOnlyActiveStores() throws Exception {
        Store activeStore1 = new Store("Active Store 1", "11.222.333/0001-81");
        setStoreId(activeStore1, 1L);

        Store activeStore2 = new Store("Active Store 2", "11.444.777/0001-61");
        setStoreId(activeStore2, 2L);

        Store inactiveStore = new Store("Inactive Store", "11222333000181");
        setStoreId(inactiveStore, 3L);
        inactiveStore.setActive(false);

        List<Store> allStores = List.of(activeStore1, activeStore2, inactiveStore);
        when(storeRepository.findAll()).thenReturn(allStores);

        List<Store> result = storeService.listActiveStores();

        assertEquals(2, result.size());
        assertTrue(result.contains(activeStore1));
        assertTrue(result.contains(activeStore2));
        assertFalse(result.contains(inactiveStore));

        result.forEach(store -> assertTrue(store.isActive()));

        verify(storeRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no active stores exist")
    void shouldReturnEmptyListWhenNoActiveStoresExist() throws Exception {
        Store inactiveStore = new Store("Inactive Store", "11.444.777/0001-61");
        setStoreId(inactiveStore, 1L);
        inactiveStore.setActive(false);

        when(storeRepository.findAll()).thenReturn(List.of(inactiveStore));

        List<Store> result = storeService.listActiveStores();

        assertTrue(result.isEmpty());
        verify(storeRepository).findAll();
    }

    @Test
    @DisplayName("Should get store by ID successfully")
    void shouldGetStoreByIdSuccessfully() {
        when(storeRepository.findById(STORE_ID)).thenReturn(Optional.of(testStore));

        Store result = storeService.getStore(STORE_ID);

        assertNotNull(result);
        assertEquals(testStore.getId(), result.getId());
        assertEquals(testStore.getCompanyName().getValue(), result.getCompanyName().getValue());
        assertEquals(testStore.getCnpj().getValue(), result.getCnpj().getValue());

        verify(storeRepository).findById(STORE_ID);
    }

    @Test
    @DisplayName("Should throw StoreNotFoundException when store does not exist")
    void shouldThrowStoreNotFoundExceptionWhenStoreDoesNotExist() {
        Long nonExistentId = 999L;
        when(storeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        StoreNotFoundException exception = assertThrows(
                StoreNotFoundException.class,
                () -> storeService.getStore(nonExistentId));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Store with id " + nonExistentId + " not found"));
        verify(storeRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should update store name successfully")
    void shouldUpdateStoreNameSuccessfully() {
        String newStoreName = "Updated Store Name";
        StoreRequest updateRequest = new StoreRequest(newStoreName, null);

        when(storeRepository.findById(STORE_ID)).thenReturn(Optional.of(testStore));
        when(storeRepository.save(any(Store.class))).thenReturn(testStore);

        Store result = storeService.updateStore(STORE_ID, updateRequest);

        assertNotNull(result);
        verify(storeRepository).findById(STORE_ID);
        verify(storeRepository).save(testStore);
    }

    @Test
    @DisplayName("Should not update store when name is null")
    void shouldNotUpdateStoreWhenNameIsNull() {
        StoreRequest updateRequest = new StoreRequest(null, null);
        StoreName originalName = testStore.getCompanyName();

        when(storeRepository.findById(STORE_ID)).thenReturn(Optional.of(testStore));
        when(storeRepository.save(any(Store.class))).thenReturn(testStore);

        Store result = storeService.updateStore(STORE_ID, updateRequest);

        assertNotNull(result);
        assertEquals(originalName, result.getCompanyName());
        verify(storeRepository).findById(STORE_ID);
        verify(storeRepository).save(testStore);
    }

    @Test
    @DisplayName("Should throw StoreNotFoundException when updating non-existent store")
    void shouldThrowStoreNotFoundExceptionWhenUpdatingNonExistentStore() {
        Long nonExistentId = 999L;
        StoreRequest updateRequest = new StoreRequest("New Name", null);
        when(storeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        StoreNotFoundException exception = assertThrows(
                StoreNotFoundException.class,
                () -> storeService.updateStore(nonExistentId, updateRequest));

        assertNotNull(exception);
        verify(storeRepository).findById(nonExistentId);
        verify(storeRepository, never()).save(any(Store.class));
    }

    @Test
    @DisplayName("Should deactivate store successfully when no active offers exist")
    void shouldDeactivateStoreSuccessfullyWhenNoActiveOffersExist() {
        when(storeRepository.findById(STORE_ID)).thenReturn(Optional.of(testStore));
        when(offerRepository.findAll()).thenReturn(List.of());
        when(storeRepository.save(any(Store.class))).thenReturn(testStore);

        Store result = storeService.deactivateStore(STORE_ID);

        assertNotNull(result);
        verify(storeRepository).findById(STORE_ID);
        verify(offerRepository).findAll();
        verify(storeRepository).save(testStore);
    }

    @Test
    @DisplayName("Should throw AlreadyDeactivatedException when store is already inactive")
    void shouldThrowAlreadyDeactivatedExceptionWhenStoreIsAlreadyInactive() {
        testStore.setActive(false);
        when(storeRepository.findById(STORE_ID)).thenReturn(Optional.of(testStore));

        AlreadyDeactivatedException exception = assertThrows(
                AlreadyDeactivatedException.class,
                () -> storeService.deactivateStore(STORE_ID));

        assertNotNull(exception);
        verify(storeRepository).findById(STORE_ID);
        verify(offerRepository, never()).findAll();
        verify(storeRepository, never()).save(any(Store.class));
    }

    @Test
    @DisplayName("Should throw EntityInUseException when store has active offers")
    void shouldThrowEntityInUseExceptionWhenStoreHasActiveOffers() {
        Offer activeOffer = new Offer();
        activeOffer.setStore(testStore);
        activeOffer.setActive(true);

        when(storeRepository.findById(STORE_ID)).thenReturn(Optional.of(testStore));
        when(offerRepository.findAll()).thenReturn(List.of(activeOffer));

        EntityInUseException exception = assertThrows(
                EntityInUseException.class,
                () -> storeService.deactivateStore(STORE_ID));

        assertNotNull(exception);
        verify(storeRepository).findById(STORE_ID);
        verify(offerRepository).findAll();
        verify(storeRepository, never()).save(any(Store.class));
    }

    @Test
    @DisplayName("Should deactivate store when offers exist but are inactive")
    void shouldDeactivateStoreWhenOffersExistButAreInactive() throws Exception {
        Store otherStore = new Store("Other Store", "11.444.777/0001-61");
        setStoreId(otherStore, 2L);

        Offer inactiveOfferSameStore = new Offer();
        inactiveOfferSameStore.setStore(testStore);
        inactiveOfferSameStore.setActive(false);

        Offer activeOfferOtherStore = new Offer();
        activeOfferOtherStore.setStore(otherStore);
        activeOfferOtherStore.setActive(true);

        when(storeRepository.findById(STORE_ID)).thenReturn(Optional.of(testStore));
        when(offerRepository.findAll()).thenReturn(List.of(inactiveOfferSameStore, activeOfferOtherStore));
        when(storeRepository.save(any(Store.class))).thenReturn(testStore);

        Store result = storeService.deactivateStore(STORE_ID);

        assertNotNull(result);
        verify(storeRepository).findById(STORE_ID);
        verify(offerRepository).findAll();
        verify(storeRepository).save(testStore);
    }

    @Test
    @DisplayName("Should throw StoreNotFoundException when deactivating non-existent store")
    void shouldThrowStoreNotFoundExceptionWhenDeactivatingNonExistentStore() {
        Long nonExistentId = 999L;
        when(storeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        StoreNotFoundException exception = assertThrows(
                StoreNotFoundException.class,
                () -> storeService.deactivateStore(nonExistentId));

        assertNotNull(exception);
        verify(storeRepository).findById(nonExistentId);
        verify(offerRepository, never()).findAll();
        verify(storeRepository, never()).save(any(Store.class));
    }

    @Test
    @DisplayName("Should reactivate store successfully")
    void shouldReactivateStoreSuccessfully() {
        testStore.setActive(false);
        when(storeRepository.findById(STORE_ID)).thenReturn(Optional.of(testStore));
        when(storeRepository.save(any(Store.class))).thenReturn(testStore);

        Store result = storeService.reactivateStore(STORE_ID);

        assertNotNull(result);
        verify(storeRepository).findById(STORE_ID);
        verify(storeRepository).save(testStore);
    }

    @Test
    @DisplayName("Should throw AlreadyActiveException when store is already active")
    void shouldThrowAlreadyActiveExceptionWhenStoreIsAlreadyActive() {
        when(storeRepository.findById(STORE_ID)).thenReturn(Optional.of(testStore));

        AlreadyActiveException exception = assertThrows(
                AlreadyActiveException.class,
                () -> storeService.reactivateStore(STORE_ID));

        assertNotNull(exception);
        verify(storeRepository).findById(STORE_ID);
        verify(storeRepository, never()).save(any(Store.class));
    }

    @Test
    @DisplayName("Should throw StoreNotFoundException when reactivating non-existent store")
    void shouldThrowStoreNotFoundExceptionWhenReactivatingNonExistentStore() {
        Long nonExistentId = 999L;
        when(storeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        StoreNotFoundException exception = assertThrows(
                StoreNotFoundException.class,
                () -> storeService.reactivateStore(nonExistentId));

        assertNotNull(exception);
        verify(storeRepository).findById(nonExistentId);
        verify(storeRepository, never()).save(any(Store.class));
    }

    @Test
    @DisplayName("Should handle store registration with different CNPJ formats")
    void shouldHandleStoreRegistrationWithDifferentCnpjFormats() {
        String cnpjWithoutMask = "11222333000181"; // Valid CNPJ without mask
        StoreRequest requestWithoutMask = new StoreRequest("Store Without Mask", cnpjWithoutMask);
        Store storeWithoutMask = new Store("Store Without Mask", cnpjWithoutMask);

        when(storeRepository.findByCnpj(cnpjWithoutMask)).thenReturn(Optional.empty());
        when(storeRepository.save(any(Store.class))).thenReturn(storeWithoutMask);

        Store result = storeService.registerStore(requestWithoutMask);

        assertNotNull(result);
        verify(storeRepository).findByCnpj(cnpjWithoutMask);
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    @DisplayName("Should verify repository interactions for successful operations")
    void shouldVerifyRepositoryInteractionsForSuccessfulOperations() {
        when(storeRepository.findByCnpj(STORE_CNPJ)).thenReturn(Optional.empty());
        when(storeRepository.save(any(Store.class))).thenReturn(testStore);
        when(storeRepository.findById(STORE_ID)).thenReturn(Optional.of(testStore));
        when(storeRepository.findAll()).thenReturn(List.of(testStore));

        storeService.registerStore(validStoreRequest);
        storeService.listActiveStores();
        storeService.getStore(STORE_ID);
        storeService.updateStore(STORE_ID, new StoreRequest("Updated Name", null));

        verify(storeRepository, times(1)).findByCnpj(STORE_CNPJ);
        verify(storeRepository, times(2)).save(any(Store.class));
        verify(storeRepository, times(1)).findAll();
        verify(storeRepository, times(2)).findById(STORE_ID);
    }

    @Test
    @DisplayName("Should handle edge case with empty store list")
    void shouldHandleEdgeCaseWithEmptyStoreList() {
        when(storeRepository.findAll()).thenReturn(List.of());

        List<Store> result = storeService.listActiveStores();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(storeRepository).findAll();
    }

    private void setStoreId(Store store, Long id) throws Exception {
        Field idField = Store.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(store, id);
    }
}