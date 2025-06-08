package com.gustavo.mobiauto_backend.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gustavo.mobiauto_backend.model.vehicle.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}