package com.gustavo.mobiauto_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.mobiauto_backend.controller.requests.VehicleRequest;
import com.gustavo.mobiauto_backend.infra.exceptions.OfferNotFoundException;
import com.gustavo.mobiauto_backend.infra.repositories.OfferRepository;
import com.gustavo.mobiauto_backend.model.offer.Offer;
import com.gustavo.mobiauto_backend.model.store.Store;
import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.model.vehicle.Vehicle;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyActiveException;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyDeactivatedException;

@Service
@Transactional(readOnly = true)
public class OfferService {
        private final OfferRepository offerRepository;
        private final StoreService storeService;
        private final UserService userService;
        private final VehicleService vehicleService;

        public OfferService(StoreService storeService, UserService userService,
                        OfferRepository offerRepository, VehicleService vehicleService) {
                this.storeService = storeService;
                this.userService = userService;
                this.offerRepository = offerRepository;
                this.vehicleService = vehicleService;
        }

        @Transactional
        public Offer registerOffer(Long storeId, Long userId, VehicleRequest request) {
                Store store = storeService.getStore(storeId);
                User client = userService.findUser(userId);

                Vehicle vehicle = vehicleService.createVehicle(request);

                Offer offer = new Offer(client, vehicle, store);
                offer = offerRepository.save(offer);

                return offer;
        }

        public Offer getOffer(Long id) {
                return offerRepository.findById(id)
                                .orElseThrow(() -> new OfferNotFoundException(id));
        }

        @Transactional
        public Offer deactivateOffer(Long id) {
                Offer offer = this.getOffer(id);

                if (!offer.isActive()) {
                        throw new AlreadyDeactivatedException(Offer.class, id);
                }

                offer.setActive(false);
                offer = offerRepository.save(offer);
                return offer;
        }

        @Transactional
        public Offer reactivateOffer(Long id) {
                Offer offer = offerRepository.findById(id)
                                .orElseThrow(() -> new OfferNotFoundException(id));

                if (offer.isActive()) {
                        throw new AlreadyActiveException(Offer.class, id);
                }

                offer.setActive(true);
                offer = offerRepository.save(offer);
                return offer;
        }
}