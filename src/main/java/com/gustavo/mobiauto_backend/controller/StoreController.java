package com.gustavo.mobiauto_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.mobiauto_backend.controller.dto.StoreDto;
import com.gustavo.mobiauto_backend.controller.requests.StoreRequest;
import com.gustavo.mobiauto_backend.service.StoreService;

@RestController
public class StoreController {
    private StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/stores")
    public ResponseEntity<StoreDto> registerStore(@RequestBody StoreRequest request) {
        return new ResponseEntity<>(StoreDto.of(storeService.registerStore(request)), HttpStatus.CREATED);
    }

    @GetMapping("/stores")
    public ResponseEntity<List<StoreDto>> listStores() {
        List<StoreDto> report = storeService.listActiveStores().stream()
                .map(StoreDto::of)
                .toList();
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/stores/{id}")
    public ResponseEntity<StoreDto> getStore(@PathVariable Long id) {
        return new ResponseEntity<>(StoreDto.of(storeService.getStore(id)), HttpStatus.OK);
    }

    @PatchMapping("/stores/{id}")
    public ResponseEntity<StoreDto> updateStore(@PathVariable Long id, @RequestBody StoreRequest request) {
        return new ResponseEntity<>(StoreDto.of(storeService.updateStore(id, request)), HttpStatus.OK);
    }

    @DeleteMapping("/stores/{id}")
    public ResponseEntity<StoreDto> deactivateStore(@PathVariable Long id) {
        return new ResponseEntity<>(StoreDto.of(storeService.deactivateStore(id)), HttpStatus.OK);
    }

    @PatchMapping("/stores/{id}/reactivate")
    public ResponseEntity<StoreDto> reactivateStore(@PathVariable Long id) {
        return new ResponseEntity<>(StoreDto.of(storeService.reactivateStore(id)), HttpStatus.OK);
    }
}
