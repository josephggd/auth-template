package com.kobe2.escrituraauth.repositories;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<EscrituraUser, UUID> {
    Optional<EscrituraUser> findByEmailAndPw(String email, String pw);
    Optional<EscrituraUser> findByEmail(String email);
    Optional<EscrituraUser> findByPw(String pw);
}
