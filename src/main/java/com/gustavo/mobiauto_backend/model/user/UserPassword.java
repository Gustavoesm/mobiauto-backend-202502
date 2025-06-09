package com.gustavo.mobiauto_backend.model.user;

import com.gustavo.mobiauto_backend.common.helpers.CryptoUtils;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class UserPassword {
    private String value;

    public UserPassword(String value) {
        setValue(value);
    }

    public void setValue(String value) {
        if (value == null || value.length() < 4) {
            throw new IllegalArgumentException("Password should be at least 4 characters long.");
        }
        this.value = CryptoUtils.encryptPassword(value);
    }
}
