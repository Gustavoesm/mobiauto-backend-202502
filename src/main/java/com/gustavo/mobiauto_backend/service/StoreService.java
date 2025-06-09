package com.gustavo.mobiauto_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.mobiauto_backend.controller.requests.StoreRequest;
import com.gustavo.mobiauto_backend.infra.exceptions.StoreNotFoundException;
import com.gustavo.mobiauto_backend.infra.repositories.OfferRepository;
import com.gustavo.mobiauto_backend.infra.repositories.StoreRepository;
import com.gustavo.mobiauto_backend.model.store.Cnpj;
import com.gustavo.mobiauto_backend.model.store.Store;
import com.gustavo.mobiauto_backend.model.store.StoreName;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyActiveException;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyDeactivatedException;
import com.gustavo.mobiauto_backend.service.exceptions.DuplicateException;
import com.gustavo.mobiauto_backend.service.exceptions.EntityInUseException;

@Service
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;
    private final OfferRepository offerRepository;

    public StoreService(StoreRepository storeRepository, OfferRepository offerRepository) {
        this.storeRepository = storeRepository;
        this.offerRepository = offerRepository;
    }

    @Transactional
    public Store registerStore(StoreRequest request) {
        if (storeRepository.findByCnpj(request.getCnpj()).isPresent()) {
            throw new DuplicateException(Cnpj.class, String.valueOf(request.getCnpj()));
        }

        Store store = new Store(request.getStoreName(), request.getCnpj());
        return storeRepository.save(store);
    }

    public List<Store> listActiveStores() {
        return storeRepository.findAll().stream()
                .filter(Store::isActive)
                .toList();
    }

    public Store getStore(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new StoreNotFoundException(id));
    }

    @Transactional
    public Store updateStore(Long id, StoreRequest request) {
        Store store = this.getStore(id);

        if (request.getStoreName() != null) {
            store.setCompanyName(new StoreName(request.getStoreName()));
        }

        return storeRepository.save(store);
    }

    @Transactional
    public Store deactivateStore(Long id) {
        Store store = this.getStore(id);

        if (!store.isActive()) {
            throw new AlreadyDeactivatedException(Store.class, id);
        }

        boolean hasActiveOffers = offerRepository.findAll().stream()
                .anyMatch(offer -> offer.getStore().getId().equals(id) && offer.isActive());

        if (hasActiveOffers) {
            throw new EntityInUseException(Store.class, id);
        }

        store.setActive(false);
        return storeRepository.save(store);
    }

    @Transactional
    public Store reactivateStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new StoreNotFoundException(id));

        if (store.isActive()) {
            throw new AlreadyActiveException(Store.class, id);
        }

        store.setActive(true);
        return storeRepository.save(store);
    }
}
