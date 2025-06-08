package com.gustavo.mobiauto_backend.service.exceptions;

public class OfferAlreadyActiveException extends RuntimeException {
    public OfferAlreadyActiveException(Long offerId) {
        super("Offer with ID " + offerId + " is already active.");
    }
}