package com.gustavo.mobiauto_backend.model.vehicle;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@AllArgsConstructor
@Getter
public class Vehicle {
    private @Id int id;
    private VehicleType type;
    private VehicleModel model;
    private VehicleReleaseYear year;
    private VehicleColor color;
}
