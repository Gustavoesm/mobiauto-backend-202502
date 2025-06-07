package com.gustavo.mobiauto_backend.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gustavo.mobiauto_backend.model.store.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
