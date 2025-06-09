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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@NoArgsConstructor
@Getter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private VehicleType type;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "model", nullable = false))
    private VehicleModel model;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "release_year", nullable = false))
    private VehicleReleaseYear releaseYear;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "color", nullable = false))
    private VehicleColor color;

    public Vehicle(VehicleType type, VehicleModel model, VehicleReleaseYear releaseYear, VehicleColor color) {
        setType(type);
        setModel(model);
        setReleaseYear(releaseYear);
        setColor(color);
    }

    public Vehicle(Long id, VehicleType type, VehicleModel model, VehicleReleaseYear releaseYear, VehicleColor color) {
        this.id = id;
        setType(type);
        setModel(model);
        setReleaseYear(releaseYear);
        setColor(color);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(VehicleType type) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        this.type = type;
    }

    public void setModel(VehicleModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Vehicle model cannot be null");
        }
        this.model = model;
    }

    public void setReleaseYear(VehicleReleaseYear releaseYear) {
        if (releaseYear == null) {
            throw new IllegalArgumentException("Vehicle release year cannot be null");
        }
        this.releaseYear = releaseYear;
    }

    public void setColor(VehicleColor color) {
        if (color == null) {
            throw new IllegalArgumentException("Vehicle color cannot be null");
        }
        this.color = color;
    }
}
