package com.gustavo.mobiauto_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.mobiauto_backend.controller.dto.StoreDto;
import com.gustavo.mobiauto_backend.controller.dto.StoreReportDto;
import com.gustavo.mobiauto_backend.controller.requests.RegisterStoreRequest;
import com.gustavo.mobiauto_backend.service.StoreService;

@RestController
public class StoreController {
    private StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/stores")
    public ResponseEntity<StoreDto> registerStore(@RequestBody RegisterStoreRequest request) {
        return new ResponseEntity<>(storeService.registerStore(request), HttpStatus.CREATED);
    }

    @GetMapping("/stores")
    public ResponseEntity<List<StoreReportDto>> listStores() {
        return new ResponseEntity<>(storeService.listAllStores(), HttpStatus.OK);
    }
}
