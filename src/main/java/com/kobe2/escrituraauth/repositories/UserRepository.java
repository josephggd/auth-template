package com.kobe2.escrituraauth.repositories;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<EscrituraUser, UUID> {
    Optional<EscrituraUser> findByUsernameAndPassword(String email, String pw);
    Optional<EscrituraUser> findByUsername(String email);
    Optional<EscrituraUser> findByPassword(String pw);
}
