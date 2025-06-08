package com.gustavo.mobiauto_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.mobiauto_backend.controller.dto.OfferDto;
import com.gustavo.mobiauto_backend.controller.requests.RegisterOfferRequest;
import com.gustavo.mobiauto_backend.service.OfferService;

@RestController
public class OfferController {
    private OfferService offersService;

    public OfferController(OfferService offersService) {
        this.offersService = offersService;
    }

    @PostMapping("/stores/{storeId}/users/{userId}/offers")
    public ResponseEntity<OfferDto> registerOffer(
            @PathVariable Long storeId,
            @PathVariable Long userId,
            @RequestBody RegisterOfferRequest request) {
        return new ResponseEntity<>(offersService.registerOffer(storeId, userId, request), HttpStatus.CREATED);
    }
}
