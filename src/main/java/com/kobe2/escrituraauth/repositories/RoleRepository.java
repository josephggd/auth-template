package com.kobe2.escrituraauth.repositories;

import com.kobe2.escrituraauth.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<UserRole, UUID> {
}
