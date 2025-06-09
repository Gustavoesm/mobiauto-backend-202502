package com.gustavo.mobiauto_backend.model.offer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("OfferStatus Tests")
class OfferStatusTest {

    @Test
    @DisplayName("Should have correct NEW status values")
    void shouldHaveCorrectNewStatusValues() {
        OfferStatus status = OfferStatus.NEW;

        assertNotNull(status);
        assertEquals("NEW", status.name());
        assertEquals("nova", status.getDescription());
    }

    @Test
    @DisplayName("Should have correct ATTENDED status values")
    void shouldHaveCorrectAttendedStatusValues() {
        OfferStatus status = OfferStatus.ATTENDED;

        assertNotNull(status);
        assertEquals("ATTENDED", status.name());
        assertEquals("em atendimento", status.getDescription());
    }

    @Test
    @DisplayName("Should have correct COMPLETED status values")
    void shouldHaveCorrectCompletedStatusValues() {
        OfferStatus status = OfferStatus.COMPLETED;

        assertNotNull(status);
        assertEquals("COMPLETED", status.name());
        assertEquals("concluida", status.getDescription());
    }

    @Test
    @DisplayName("Should have exactly three status values")
    void shouldHaveExactlyThreeStatusValues() {
        OfferStatus[] values = OfferStatus.values();

        assertNotNull(values);
        assertEquals(3, values.length);
        assertEquals(OfferStatus.NEW, values[0]);
        assertEquals(OfferStatus.ATTENDED, values[1]);
        assertEquals(OfferStatus.COMPLETED, values[2]);
    }

    @Test
    @DisplayName("Should support valueOf conversion")
    void shouldSupportValueOfConversion() {
        assertEquals(OfferStatus.NEW, OfferStatus.valueOf("NEW"));
        assertEquals(OfferStatus.ATTENDED, OfferStatus.valueOf("ATTENDED"));
        assertEquals(OfferStatus.COMPLETED, OfferStatus.valueOf("COMPLETED"));
    }

    @Test
    @DisplayName("Should maintain proper enum ordering")
    void shouldMaintainProperEnumOrdering() {
        OfferStatus[] values = OfferStatus.values();

        assertEquals(0, OfferStatus.NEW.ordinal());
        assertEquals(1, OfferStatus.ATTENDED.ordinal());
        assertEquals(2, OfferStatus.COMPLETED.ordinal());

        assertEquals(OfferStatus.NEW, values[0]);
        assertEquals(OfferStatus.ATTENDED, values[1]);
        assertEquals(OfferStatus.COMPLETED, values[2]);
    }

    @Test
    @DisplayName("Should support status progression validation")
    void shouldSupportStatusProgressionValidation() {
        OfferStatus initialStatus = OfferStatus.NEW;
        OfferStatus inProgressStatus = OfferStatus.ATTENDED;
        OfferStatus finalStatus = OfferStatus.COMPLETED;

        assertEquals("nova", initialStatus.getDescription());
        assertEquals("em atendimento", inProgressStatus.getDescription());
        assertEquals("concluida", finalStatus.getDescription());

        assertEquals(0, initialStatus.ordinal());
        assertEquals(1, inProgressStatus.ordinal());
        assertEquals(2, finalStatus.ordinal());
    }

    @Test
    @DisplayName("Should have consistent toString representation")
    void shouldHaveConsistentToStringRepresentation() {
        assertEquals("NEW", OfferStatus.NEW.toString());
        assertEquals("ATTENDED", OfferStatus.ATTENDED.toString());
        assertEquals("COMPLETED", OfferStatus.COMPLETED.toString());
    }
}