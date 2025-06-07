package com.gustavo.mobiauto_backend.service;

import org.springframework.stereotype.Service;

import com.gustavo.mobiauto_backend.controller.dto.StoreDto;
import com.gustavo.mobiauto_backend.controller.requests.RegisterStoreRequest;
import com.gustavo.mobiauto_backend.model.repositories.StoreRepository;
import com.gustavo.mobiauto_backend.model.store.Store;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public StoreDto registerStore(RegisterStoreRequest request) {
        Store store = new Store(request.getStoreName());
        store = storeRepository.save(store);
        return StoreDto.of(store);
    }
}
