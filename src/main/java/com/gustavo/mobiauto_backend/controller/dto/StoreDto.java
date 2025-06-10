package com.gustavo.mobiauto_backend.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gustavo.mobiauto_backend.common.helpers.Formatters;
import com.gustavo.mobiauto_backend.model.offer.Offer;
import com.gustavo.mobiauto_backend.model.store.Store;
import com.gustavo.mobiauto_backend.service.exceptions.DeactivatedStoreException;

import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "name", "cnpj", "totalOffers", "offers" })
public class StoreDto {
    private Long id;
    private String name;
    private String cnpj;
    private long totalOffers;
    private List<OfferDto> offers;

    private StoreDto(Long id, String name, String cnpj, long offerCount, List<OfferDto> offers) {
        this.id = id;
        this.name = name;
        this.cnpj = Formatters.formatCnpj(cnpj);
        this.totalOffers = offerCount;
        this.offers = offers;
    }

    public static StoreDto of(Store store) {
        if (!store.isActive())
            throw new DeactivatedStoreException(store.getId());

        List<OfferDto> offers = store.getOffers().stream()
                .filter(Offer::isActive)
                .map(OfferDto::of)
                .toList();
        return new StoreDto(
                store.getId(),
                store.getCompanyName().getValue(),
                store.getCnpj().getValue(),
                offers.size(), offers);
    }
}