package com.gustavo.mobiauto_backend.controller.dto;

import lombok.Data;

@Data
public class VehicleDto {
    private String model;
    private String color;
    private Integer releaseYear;
    private String type;

    private VehicleDto(String model, String color, Integer releaseYear, String type) {
        this.model = model;
        this.color = color;
        this.releaseYear = releaseYear;
        this.type = type;
    }

    public static VehicleDto of(com.gustavo.mobiauto_backend.model.vehicle.Vehicle vehicle) {
        return new VehicleDto(
                vehicle.getModel().getValue(),
                vehicle.getColor().getValue(),
                vehicle.getReleaseYear().getValue(),
                vehicle.getType().getValue());
    }
}
