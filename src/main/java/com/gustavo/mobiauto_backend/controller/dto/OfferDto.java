package com.gustavo.mobiauto_backend.controller.dto;

import com.gustavo.mobiauto_backend.common.helpers.Formatters;
import com.gustavo.mobiauto_backend.model.offer.Offer;
import com.gustavo.mobiauto_backend.model.offer.OfferStatus;
import com.gustavo.mobiauto_backend.service.exceptions.DeactivatedOfferException;

import lombok.Data;

@Data
public class OfferDto {
    private OfferStatus status;
    private UserDto client;
    private VehicleDto vehicle;
    private String storeCnpj;
    private String storeName;

    private OfferDto(OfferStatus status, UserDto client, VehicleDto vehicle, Long storeCnpj, String storeName) {
        this.status = status;
        this.client = client;
        this.vehicle = vehicle;
        this.storeCnpj = Formatters.formatCnpj(storeCnpj);
        this.storeName = storeName;
    }

    public static OfferDto of(Offer offer) {
        if (!offer.isActive())
            throw new DeactivatedOfferException(offer.getId());

        return new OfferDto(
                offer.getStatus(),
                UserDto.of(offer.getClient()),
                VehicleDto.of(offer.getVehicle()),
                offer.getStore().getCnpj().getValue(),
                offer.getStore().getCompanyName().getValue());
    }
}
