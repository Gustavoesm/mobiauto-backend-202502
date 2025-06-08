package com.gustavo.mobiauto_backend.model.vehicle;

import java.time.Year;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class VehicleReleaseYear {
    private @Getter int value;

    public void setValue(int value) {
        if (value > Year.now().getValue()) {
            throw new IllegalArgumentException("Vehicle release year cannot be in the future. Provided: " + value);
        }
        if (value < 1900) {
            throw new IllegalArgumentException("Vehicle release year must be after 1900. Provided: " + value);
        }
        this.value = value;
    }

    public VehicleReleaseYear(int value) {
        setValue(value);
    }
}
