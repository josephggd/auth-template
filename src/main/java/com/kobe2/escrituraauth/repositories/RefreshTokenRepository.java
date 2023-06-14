package com.kobe2.escrituraauth.repositories;

import com.kobe2.escrituraauth.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByCode(UUID uuid);
    Optional<RefreshToken> findByUserId(UUID uuid);
}
