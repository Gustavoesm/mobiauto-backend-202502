package com.gustavo.mobiauto_backend.service;

import org.springframework.stereotype.Service;

import com.gustavo.mobiauto_backend.controller.dto.OfferDto;
import com.gustavo.mobiauto_backend.controller.requests.RegisterOfferRequest;
import com.gustavo.mobiauto_backend.model.exceptions.StoreNotFoundException;
import com.gustavo.mobiauto_backend.model.exceptions.UserNotFoundException;
import com.gustavo.mobiauto_backend.model.offer.Offer;
import com.gustavo.mobiauto_backend.model.repositories.OfferRepository;
import com.gustavo.mobiauto_backend.model.repositories.StoreRepository;
import com.gustavo.mobiauto_backend.model.repositories.UserRepository;
import com.gustavo.mobiauto_backend.model.repositories.VehicleRepository;
import com.gustavo.mobiauto_backend.model.store.Store;
import com.gustavo.mobiauto_backend.model.user.User;
import com.gustavo.mobiauto_backend.model.vehicle.Vehicle;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleColor;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleModel;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleReleaseYear;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleType;

@Service
public class OfferService {
        private final StoreRepository storeRepository;
        private final UserRepository userRepository;
        private final OfferRepository offerRepository;
        private final VehicleRepository vehicleRepository;

        public OfferService(StoreRepository storeRepository, UserRepository userRepository,
                        OfferRepository offerRepository, VehicleRepository vehicleRepository) {
                this.storeRepository = storeRepository;
                this.userRepository = userRepository;
                this.offerRepository = offerRepository;
                this.vehicleRepository = vehicleRepository;
        }

        public OfferDto registerOffer(Long storeId, Long userId, RegisterOfferRequest request) {
                Store store = storeRepository.findById(storeId)
                                .orElseThrow(() -> new StoreNotFoundException(storeId));

                User client = userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(userId));

                Vehicle vehicle = new Vehicle(
                                VehicleType.valueOf(request.getType().toUpperCase()),
                                new VehicleModel(request.getModel()),
                                new VehicleReleaseYear(request.getReleaseYear()),
                                new VehicleColor(request.getColor()));

                vehicle = vehicleRepository.save(vehicle);

                Offer offer = new Offer(client, vehicle, store);
                offer = offerRepository.save(offer);

                return OfferDto.of(offer);
        }
}