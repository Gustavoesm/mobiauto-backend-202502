package com.gustavo.mobiauto_backend.validation;

import java.time.Year;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MaxCurrentYearValidator implements ConstraintValidator<MaxCurrentYear, Integer> {

    @Override
    public void initialize(MaxCurrentYear constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        int currentYear = Year.now().getValue();
        return value <= currentYear;
    }
}