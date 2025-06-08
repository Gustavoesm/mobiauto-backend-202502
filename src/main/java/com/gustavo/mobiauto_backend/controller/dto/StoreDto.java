package com.gustavo.mobiauto_backend.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gustavo.mobiauto_backend.infra.config.Formatters;
import com.gustavo.mobiauto_backend.model.store.Store;
import com.gustavo.mobiauto_backend.service.exceptions.DeactivatedStoreException;

import lombok.Data;

@Data
@JsonPropertyOrder({ "name", "cnpj", "totalOffers", "offers" })
public class StoreDto {
    private String name;
    private String cnpj;
    private long totalOffers;
    private List<OfferDto> offers;

    private StoreDto(String name, long cnpj, long offerCount, List<OfferDto> offers) {
        this.name = name;
        this.cnpj = Formatters.formatCnpj(cnpj);
        this.totalOffers = offerCount;
        this.offers = offers;
    }

    public static StoreDto of(Store store) {
        if (!store.isEnabled())
            throw new DeactivatedStoreException(store.getId());

        List<OfferDto> offers = store.getOffers() != null ? store.getOffers().stream()
                .map(OfferDto::of)
                .toList() : List.of();
        return new StoreDto(
                store.getCompanyName().getValue(),
                store.getCnpj().getValue(),
                offers.size(), offers);
    }
}