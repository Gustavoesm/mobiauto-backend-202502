package com.gustavo.mobiauto_backend.controller.dto;

import com.gustavo.mobiauto_backend.model.offer.Offer;
import com.gustavo.mobiauto_backend.model.offer.OfferStatus;

import lombok.Data;

@Data
public class OfferDto {
    private Long id;
    private OfferStatus status;
    private UserDto client;
    private VehicleDto vehicle;
    private StoreDto store;

    private OfferDto(Long id, OfferStatus status, UserDto client, VehicleDto vehicle, StoreDto store) {
        this.id = id;
        this.status = status;
        this.client = client;
        this.vehicle = vehicle;
        this.store = store;
    }

    public static OfferDto of(Offer offer) {
        return new OfferDto(
                offer.getId(),
                offer.getStatus(),
                UserDto.of(offer.getClient()),
                VehicleDto.of(offer.getVehicle()),
                StoreDto.of(offer.getStore()));
    }
}
