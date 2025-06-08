package com.gustavo.mobiauto_backend.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gustavo.mobiauto_backend.infra.config.Formatters;
import com.gustavo.mobiauto_backend.model.store.Store;

import lombok.Data;

@Data
@JsonPropertyOrder({ "name", "cnpj", "totalOffers", "offers" })
public class StoreReportDto {
    private String name;
    private String cnpj;
    private long totalOffers;
    private List<OfferDto> offers;

    private StoreReportDto(String name, long cnpj, long offerCount, List<OfferDto> offers) {
        this.name = name;
        this.cnpj = Formatters.formatCnpj(cnpj);
        this.totalOffers = offerCount;
        this.offers = offers;
    }

    public static StoreReportDto of(Store store) {
        List<OfferDto> offers = store.getOffers() != null ? store.getOffers().stream()
                .map(OfferDto::of)
                .toList() : List.of();
        return new StoreReportDto(
                store.getCompanyName().getValue(),
                store.getCnpj().getValue(),
                offers.size(), offers);
    }
}