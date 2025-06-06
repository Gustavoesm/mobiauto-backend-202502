package com.gustavo.mobiauto_backend.model.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OfferStatus {
    NEW("nova"),
    IN_PROGRESS("em progresso"),
    COMPLETED("concluida");

    private final String value;
}
