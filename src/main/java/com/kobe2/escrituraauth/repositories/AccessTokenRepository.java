package com.kobe2.escrituraauth.repositories;

import com.kobe2.escrituraauth.entities.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, UUID> {
    Optional<AccessToken> findByCode(UUID uuid);
}
