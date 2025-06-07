package com.gustavo.mobiauto_backend.controller.dto;

import com.gustavo.mobiauto_backend.model.store.Store;

import lombok.Data;

@Data
public class StoreDto {
    private Long id;
    private String name;

    private StoreDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StoreDto of(Store store) {
        return new StoreDto(store.getId(), store.getName().getValue());
    }
}
