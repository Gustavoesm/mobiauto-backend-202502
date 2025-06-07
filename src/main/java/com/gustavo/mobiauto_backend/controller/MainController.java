package com.gustavo.mobiauto_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.mobiauto_backend.controller.dto.OfferDto;
import com.gustavo.mobiauto_backend.controller.dto.StoreDto;
import com.gustavo.mobiauto_backend.controller.dto.UserDto;
import com.gustavo.mobiauto_backend.controller.requests.RegisterOfferRequest;
import com.gustavo.mobiauto_backend.controller.requests.RegisterStoreRequest;
import com.gustavo.mobiauto_backend.controller.requests.RegisterUserRequest;
import com.gustavo.mobiauto_backend.service.OfferService;
import com.gustavo.mobiauto_backend.service.StoreService;
import com.gustavo.mobiauto_backend.service.UserService;

@RestController
public class MainController {
    private StoreService storeService;
    private OfferService offersService;
    private UserService userService;

    public MainController(StoreService storeService, OfferService offersService, UserService userService) {
        this.storeService = storeService;
        this.offersService = offersService;
        this.userService = userService;
    }

    @PostMapping("/stores")
    public ResponseEntity<StoreDto> registerStore(@RequestBody RegisterStoreRequest request) {
        return new ResponseEntity<>(storeService.registerStore(request), HttpStatus.CREATED);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserRequest request) {
        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/stores/{storeId}/users/{userId}/offers")
    public ResponseEntity<OfferDto> registerOffer(
            @PathVariable Long storeId,
            @PathVariable Long userId,
            @RequestBody RegisterOfferRequest request) {
        return new ResponseEntity<>(offersService.registerOffer(storeId, userId, request), HttpStatus.CREATED);
    }
}
