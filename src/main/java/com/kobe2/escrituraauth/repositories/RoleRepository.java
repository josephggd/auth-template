package com.kobe2.escrituraauth.repositories;

import com.kobe2.escrituraauth.entities.UserRole;
import com.kobe2.escrituraauth.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<UserRole, UUID> {
    List<UserRole> findAllByRole(Roles role);
}
