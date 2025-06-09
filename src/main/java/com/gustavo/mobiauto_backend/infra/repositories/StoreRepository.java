package com.gustavo.mobiauto_backend.infra.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gustavo.mobiauto_backend.model.store.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s WHERE s.cnpj.value = :cnpj")
    Optional<Store> findByCnpj(@Param("cnpj") Long cnpj);
}
