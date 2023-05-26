package com.kobe2.escrituraauth.repositories;

import com.kobe2.escrituraauth.entities.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CodeRepository extends JpaRepository<ConfirmationCode, UUID> {
    List<ConfirmationCode> findAllByCode(UUID code);
}
