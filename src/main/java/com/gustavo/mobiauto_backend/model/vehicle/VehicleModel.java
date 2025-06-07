package com.gustavo.mobiauto_backend.model.vehicle;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class VehicleModel {
    private @Getter @Setter String value;
}
