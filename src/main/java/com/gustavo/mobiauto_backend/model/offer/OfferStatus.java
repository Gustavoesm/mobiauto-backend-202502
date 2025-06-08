package com.gustavo.mobiauto_backend.model.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OfferStatus {
    NEW("nova"),
    ATTENDED("em atendimento"),
    COMPLETED("concluida");

    private final String description;
}
