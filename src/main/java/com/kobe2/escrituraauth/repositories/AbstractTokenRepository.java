package com.kobe2.escrituraauth.repositories;

import com.kobe2.escrituraauth.entities.AbstractToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AbstractTokenRepository<T> extends JpaRepository<AbstractToken, UUID> {
}
