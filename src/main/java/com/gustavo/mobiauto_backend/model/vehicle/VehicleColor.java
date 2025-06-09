package com.gustavo.mobiauto_backend.model.vehicle;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class VehicleColor {
    private @Getter String value;

    public VehicleColor(String value) {
        setValue(value);
    }

    public void setValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle color cannot be null or empty");
        }
        this.value = value.trim();
    }
}
