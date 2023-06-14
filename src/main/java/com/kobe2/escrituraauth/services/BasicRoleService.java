package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.entities.UserRole;
import com.kobe2.escrituraauth.enums.Roles;
import com.kobe2.escrituraauth.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class BasicRoleService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final RoleRepository roleRepository;
    public EscrituraUser setRole(EscrituraUser user, Roles roleName) {
        logger.log(Level.INFO, "setRole");
        UserRole userRole = new UserRole(roleName, user);
        roleRepository.save(userRole);
        Set<UserRole> currentRoles = user.getRoles();
        currentRoles.add(userRole);
        user.setRoles(currentRoles);
        return user;
    }
}
