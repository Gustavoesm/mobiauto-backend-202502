package com.gustavo.mobiauto_backend.controller.requests;

import com.gustavo.mobiauto_backend.validation.MaxCurrentYear;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterOfferRequest {
    @NotBlank(message = "Vehicle type is required")
    private String type;

    @NotBlank(message = "Vehicle model is required")
    private String model;

    @NotNull
    @Min(value = 1900, message = "Vehicle release year must be after 1900")
    @MaxCurrentYear(message = "Vehicle release year cannot be in the future")
    private Integer releaseYear;

    @NotBlank(message = "Vehicle color is required")
    private String color;

}
