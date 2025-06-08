package com.gustavo.mobiauto_backend.controller.requests;

import lombok.Data;

@Data
public class VehicleRequest {
    private String type;
    private String model;
    private Integer releaseYear;
    private String color;
}
