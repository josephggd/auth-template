package com.kobe2.escrituraauth.repositories;

import com.kobe2.escrituraauth.entities.AbstractToken;
import com.kobe2.escrituraauth.entities.ConfirmationToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationTokenRepository<T extends AbstractToken> extends AbstractTokenRepository<T> {
    Optional<ConfirmationToken> findByCode(UUID code);
}
