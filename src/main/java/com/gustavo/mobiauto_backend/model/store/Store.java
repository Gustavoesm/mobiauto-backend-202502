package com.gustavo.mobiauto_backend.model.store;

import com.gustavo.mobiauto_backend.model.offer.Offer;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Store {
    private @Id int id;
    private StoreName name;
    private Offer[] offers;

    Store(String storeName) {
        this.name = new StoreName(storeName);
    }
}
