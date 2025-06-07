package com.gustavo.mobiauto_backend.model.vehicle;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VehicleType type;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "model"))
    private VehicleModel model;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "release_year"))
    private VehicleReleaseYear releaseYear;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "color"))
    private VehicleColor color;

    public Vehicle(VehicleType type, VehicleModel model, VehicleReleaseYear releaseYear, VehicleColor color) {
        this.type = type;
        this.model = model;
        this.releaseYear = releaseYear;
        this.color = color;
    }
}
