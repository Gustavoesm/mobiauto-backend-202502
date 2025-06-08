package com.gustavo.mobiauto_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gustavo.mobiauto_backend.controller.dto.StoreDto;
import com.gustavo.mobiauto_backend.controller.dto.StoreReportDto;
import com.gustavo.mobiauto_backend.controller.requests.RegisterStoreRequest;
import com.gustavo.mobiauto_backend.infra.repositories.StoreRepository;
import com.gustavo.mobiauto_backend.model.store.Store;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public StoreDto registerStore(RegisterStoreRequest request) {
        Store store = new Store(request.getStoreName(), request.getCnpj());
        store = storeRepository.save(store);
        return StoreDto.of(store);
    }

    public List<StoreReportDto> listAllStores() {
        return storeRepository.findAll()
                .stream()
                .map(StoreReportDto::of)
                .toList();
    }
}
