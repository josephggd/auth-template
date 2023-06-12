package com.kobe2.escrituraauth.repositories;

import com.kobe2.escrituraauth.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID> {
    Optional<ConfirmationToken> findByCode(@Param("code") UUID uuid);
    void deleteAllByUserId(@Param("user_id") UUID userId);
}
