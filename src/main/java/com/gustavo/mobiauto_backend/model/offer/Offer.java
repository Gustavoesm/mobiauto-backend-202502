package com.gustavo.mobiauto_backend.model.offer;

import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.model.vehicle.Vehicle;

import jakarta.persistence.Id;

public class Offer {
    private @Id int id;
    private OfferStatus status;
    private User client;
    private Vehicle vehicle;
}
