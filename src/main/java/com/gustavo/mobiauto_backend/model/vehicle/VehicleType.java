package com.gustavo.mobiauto_backend.model.vehicle;

public enum VehicleType {
    CAR("carro"),
    MOTORCYCLE("motocicleta"),
    TRUCK("caminhão"),
    OTHER("outro");

    private final String value;

    VehicleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
