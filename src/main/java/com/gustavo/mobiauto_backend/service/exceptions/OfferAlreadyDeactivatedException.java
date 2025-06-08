package com.gustavo.mobiauto_backend.service.exceptions;

public class OfferAlreadyDeactivatedException extends RuntimeException {
    public OfferAlreadyDeactivatedException(Long offerId) {
        super("Offer with ID " + offerId + " is already deactivated.");
    }
}