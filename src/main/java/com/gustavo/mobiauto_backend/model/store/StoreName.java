package com.gustavo.mobiauto_backend.model.store;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class StoreName {
    private @Getter String value;

    public StoreName(String value) {
        setValue(value);
    }

    public void setValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Store name cannot be null or empty");
        }
        this.value = value.trim();
    }
}
