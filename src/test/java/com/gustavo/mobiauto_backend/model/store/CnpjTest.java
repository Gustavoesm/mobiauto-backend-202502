package com.gustavo.mobiauto_backend.model.store;

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
@DisplayName("Cnpj Tests")
class CnpjTest {

    @Test
    @DisplayName("Should create Cnpj with valid value")
    void shouldCreateCnpjWithValidValue() {
        String cnpjValue = "11.222.333/0001-81";
        Cnpj cnpj = new Cnpj(cnpjValue);

        assertNotNull(cnpj);
        assertEquals(cnpjValue, cnpj.getValue());
    }

    @Test
    @DisplayName("Should create Cnpj with no-args constructor")
    void shouldCreateCnpjWithNoArgsConstructor() {
        Cnpj cnpj = new Cnpj();

        assertNotNull(cnpj);
        assertNull(cnpj.getValue());
    }

    @Test
    @DisplayName("Should set and get value correctly")
    void shouldSetAndGetValueCorrectly() {
        Cnpj cnpj = new Cnpj();
        String cnpjValue = "11.222.333/0001-81";

        cnpj.setValue(cnpjValue);

        assertEquals(cnpjValue, cnpj.getValue());
    }

    @Test
    @DisplayName("Should accept valid CNPJ with mask")
    void shouldAcceptValidCnpjWithMask() {
        String cnpjWithMask = "11.222.333/0001-81";
        Cnpj cnpj = new Cnpj(cnpjWithMask);

        assertEquals(cnpjWithMask, cnpj.getValue());
    }

    @Test
    @DisplayName("Should accept valid CNPJ without mask")
    void shouldAcceptValidCnpjWithoutMask() {
        String cnpjWithoutMask = "11222333000181";
        Cnpj cnpj = new Cnpj(cnpjWithoutMask);

        assertEquals(cnpjWithoutMask, cnpj.getValue());
    }

    @Test
    @DisplayName("Should throw exception for null CNPJ")
    void shouldThrowExceptionForNullCnpj() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Cnpj(null));

        assertEquals("Please inform a valid CNPJ.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty CNPJ")
    void shouldThrowExceptionForEmptyCnpj() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Cnpj(""));

        assertEquals("Please inform a valid CNPJ.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for whitespace only CNPJ")
    void shouldThrowExceptionForWhitespaceOnlyCnpj() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Cnpj("   "));

        assertEquals("Please inform a valid CNPJ.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for invalid CNPJ with same digits")
    void shouldThrowExceptionForInvalidCnpjWithSameDigits() {
        String invalidCnpj = "11.111.111/1111-11";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Cnpj(invalidCnpj));

        assertEquals("Please inform a valid CNPJ.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for CNPJ with invalid check digits")
    void shouldThrowExceptionForCnpjWithInvalidCheckDigits() {
        String invalidCnpj = "11.222.333/0001-99";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Cnpj(invalidCnpj));

        assertEquals("Please inform a valid CNPJ.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for CNPJ with letters")
    void shouldThrowExceptionForCnpjWithLetters() {
        String invalidCnpj = "11.222.333/000A-81";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Cnpj(invalidCnpj));

        assertEquals("Please inform a valid CNPJ.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for CNPJ with wrong length")
    void shouldThrowExceptionForCnpjWithWrongLength() {
        String shortCnpj = "11.222.333/001-81";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Cnpj(shortCnpj));

        assertEquals("Please inform a valid CNPJ.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when setting invalid CNPJ via setter")
    void shouldThrowExceptionWhenSettingInvalidCnpjViaSetter() {
        Cnpj cnpj = new Cnpj();
        String invalidCnpj = "00.000.000/0000-00";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cnpj.setValue(invalidCnpj));

        assertEquals("Please inform a valid CNPJ.", exception.getMessage());
    }

    @Test
    @DisplayName("Should accept different valid CNPJ formats")
    void shouldAcceptDifferentValidCnpjFormats() {
        String cnpj1 = "11.222.333/0001-81";
        String cnpj2 = "11222333000181";
        String cnpj3 = "11.444.777/0001-61";

        Cnpj cnpjObj1 = new Cnpj(cnpj1);
        Cnpj cnpjObj2 = new Cnpj(cnpj2);
        Cnpj cnpjObj3 = new Cnpj(cnpj3);

        assertEquals(cnpj1, cnpjObj1.getValue());
        assertEquals(cnpj2, cnpjObj2.getValue());
        assertEquals(cnpj3, cnpjObj3.getValue());
    }

    @Test
    @DisplayName("Should throw exception for too long CNPJ")
    void shouldThrowExceptionForTooLongCnpj() {
        String tooLongCnpj = "111.222.333/0001-811";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Cnpj(tooLongCnpj));

        assertEquals("Please inform a valid CNPJ.", exception.getMessage());
    }

    @Test
    @DisplayName("Should set valid CNPJ via setter")
    void shouldSetValidCnpjViaSetter() {
        Cnpj cnpj = new Cnpj();
        String validCnpj = "11.222.333/0001-81";

        cnpj.setValue(validCnpj);

        assertEquals(validCnpj, cnpj.getValue());
    }
}