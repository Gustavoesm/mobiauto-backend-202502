package com.gustavo.mobiauto_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.mobiauto_backend.controller.requests.VehicleRequest;
import com.gustavo.mobiauto_backend.infra.exceptions.VehicleNotFoundException;
import com.gustavo.mobiauto_backend.infra.repositories.VehicleRepository;
import com.gustavo.mobiauto_backend.model.vehicle.Vehicle;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleColor;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleModel;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleReleaseYear;
import com.gustavo.mobiauto_backend.model.vehicle.VehicleType;

@Service
@Transactional(readOnly = true)
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public Vehicle createVehicle(VehicleRequest request) {
        Vehicle vehicle = new Vehicle(
                VehicleType.valueOf(request.getType().toUpperCase()),
                new VehicleModel(request.getModel()),
                new VehicleReleaseYear(request.getReleaseYear()),
                new VehicleColor(request.getColor()));
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicle(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }

    @Transactional
    public Vehicle updateVehicle(Long id, VehicleRequest request) {
        Vehicle vehicle = this.getVehicle(id);

        if (request.getType() != null) {
            vehicle.setType(VehicleType.valueOf(request.getType().toUpperCase()));
        }

        if (request.getModel() != null) {
            vehicle.setModel(new VehicleModel(request.getModel()));
        }

        if (request.getReleaseYear() != null) {
            vehicle.setReleaseYear(new VehicleReleaseYear(request.getReleaseYear()));
        }

        if (request.getColor() != null) {
            vehicle.setColor(new VehicleColor(request.getColor()));
        }

        return vehicleRepository.save(vehicle);
    }
}