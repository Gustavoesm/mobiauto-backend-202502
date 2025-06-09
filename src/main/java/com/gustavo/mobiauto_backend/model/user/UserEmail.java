package com.gustavo.mobiauto_backend.model.user;

import com.gustavo.mobiauto_backend.common.helpers.Validation;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class UserEmail {
    private @Getter String value;

    public UserEmail(String value) {
        setValue(value);
    }

    public void setValue(String value) {
        if (value == null || value.trim().isEmpty() || !value.matches(Validation.EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.value = value.trim().toLowerCase();
    }
}
