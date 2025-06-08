package com.gustavo.mobiauto_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.mobiauto_backend.controller.dto.OfferDto;
import com.gustavo.mobiauto_backend.controller.requests.VehicleRequest;
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
            @RequestBody VehicleRequest request) {
        return new ResponseEntity<>(
                OfferDto.of(offersService.registerOffer(storeId, userId, request)),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/offers/{id}")
    public ResponseEntity<Void> deactivateOffer(@PathVariable Long id) {
        offersService.deactivateOffer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/offers/{id}/reactivate")
    public ResponseEntity<OfferDto> reactivateOffer(@PathVariable Long id) {
        return new ResponseEntity<>(
                OfferDto.of(offersService.reactivateOffer(id)),
                HttpStatus.OK);
    }
}
