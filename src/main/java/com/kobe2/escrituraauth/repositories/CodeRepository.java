package com.kobe2.escrituraauth.repositories;

import com.kobe2.escrituraauth.entities.AuthenticationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CodeRepository extends JpaRepository<AuthenticationCode, UUID> {
    Optional<AuthenticationCode> findByCode(UUID code);
}
