package com.gustavo.mobiauto_backend.infra.exceptions;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(Long id) {
        super("Vehicle with id " + id + " not found.");
    }
}