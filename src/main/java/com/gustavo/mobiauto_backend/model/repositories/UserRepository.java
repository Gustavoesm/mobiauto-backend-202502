package com.gustavo.mobiauto_backend.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gustavo.mobiauto_backend.model.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
