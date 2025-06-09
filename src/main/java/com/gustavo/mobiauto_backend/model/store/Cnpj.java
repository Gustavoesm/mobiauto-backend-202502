package com.gustavo.mobiauto_backend.model.store;

import com.gustavo.mobiauto_backend.common.helpers.Validation;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class Cnpj {
    private @Getter String value;

    public Cnpj(String value) {
        setValue(value);
    }

    public void setValue(String value) {
        if (!Validation.isValidCNPJ(value)) {
            throw new IllegalArgumentException("Please inform a valid CNPJ.");
        }
        this.value = value;
    }
}
