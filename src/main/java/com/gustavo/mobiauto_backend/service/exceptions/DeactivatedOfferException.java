package com.gustavo.mobiauto_backend.service.exceptions;

public class DeactivatedOfferException extends RuntimeException {
    public DeactivatedOfferException(Long offerId) {
        super("Offer with ID " + offerId + " is deactivated.");
    }
}