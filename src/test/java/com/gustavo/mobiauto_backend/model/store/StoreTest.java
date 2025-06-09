package com.gustavo.mobiauto_backend.model.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Store Tests")
class StoreTest {

    private Store store;
    private String storeName;
    private String cnpj;

    @BeforeEach
    void setUp() {
        storeName = "Loja de Carros ABC";
        cnpj = "25.215.908/0001-43";
    }

    @Test
    @DisplayName("Should create store with all parameters")
    void shouldCreateStoreWithAllParameters() {
        store = new Store(storeName, cnpj);

        assertNotNull(store);
        assertEquals(storeName, store.getCompanyName().getValue());
        assertEquals(cnpj, store.getCnpj().getValue());
        assertTrue(store.isActive());
        assertNotNull(store.getOffers());
        assertTrue(store.getOffers().isEmpty());
        assertNull(store.getId());
    }

    @Test
    @DisplayName("Should create store using no-args constructor")
    void shouldCreateStoreUsingNoArgsConstructor() {
        store = new Store();

        assertNotNull(store);
        assertNull(store.getCompanyName());
        assertNull(store.getCnpj());
        assertFalse(store.isActive());
        assertNull(store.getOffers());
        assertNull(store.getId());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllPropertiesCorrectly() {
        store = new Store();
        StoreName newCompanyName = new StoreName("Nova Loja XYZ");
        boolean active = true;

        store.setCompanyName(newCompanyName);
        store.setActive(active);

        assertEquals(newCompanyName, store.getCompanyName());
        assertEquals(active, store.isActive());
    }

    @Test
    @DisplayName("Should handle store activation and deactivation")
    void shouldHandleStoreActivationAndDeactivation() {
        store = new Store(storeName, cnpj);

        assertTrue(store.isActive());

        store.setActive(false);
        assertFalse(store.isActive());

        store.setActive(true);
        assertTrue(store.isActive());
    }

    @Test
    @DisplayName("Should throw exception when creating store with null store name")
    void shouldThrowExceptionWhenCreatingStoreWithNullStoreName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Store(null, cnpj));

        assertEquals("Store name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating store with empty store name")
    void shouldThrowExceptionWhenCreatingStoreWithEmptyStoreName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Store("", cnpj));

        assertEquals("Store name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating store with whitespace only store name")
    void shouldThrowExceptionWhenCreatingStoreWithWhitespaceOnlyStoreName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Store("   ", cnpj));

        assertEquals("Store name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating store with invalid CNPJ")
    void shouldThrowExceptionWhenCreatingStoreWithInvalidCnpj() {
        String invalidCnpj = "11.111.111/0001-11";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Store(storeName, invalidCnpj));

        assertEquals("Please inform a valid CNPJ.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating store with null CNPJ")
    void shouldThrowExceptionWhenCreatingStoreWithNullCnpj() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Store(storeName, null));

        assertEquals("Please inform a valid CNPJ.", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle different valid CNPJ formats")
    void shouldHandleDifferentValidCnpjFormats() {
        String cnpjWithMask = "11.222.333/0001-81";
        String cnpjWithoutMask = "11222333000181";

        Store store1 = new Store(storeName, cnpjWithMask);
        Store store2 = new Store("Outra Loja", cnpjWithoutMask);

        assertEquals(cnpjWithMask, store1.getCnpj().getValue());
        assertEquals(cnpjWithoutMask, store2.getCnpj().getValue());
    }

    @Test
    @DisplayName("Should set company name correctly")
    void shouldSetCompanyNameCorrectly() {
        store = new Store();
        StoreName companyName = new StoreName("Loja Teste");

        store.setCompanyName(companyName);

        assertEquals(companyName, store.getCompanyName());
        assertEquals("Loja Teste", store.getCompanyName().getValue());
    }

    @Test
    @DisplayName("Should initialize offers as empty list")
    void shouldInitializeOffersAsEmptyList() {
        store = new Store(storeName, cnpj);

        assertNotNull(store.getOffers());
        assertTrue(store.getOffers().isEmpty());
        assertEquals(0, store.getOffers().size());
    }
}