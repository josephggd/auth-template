package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UnauthenticatedRoleService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final BasicRoleService basicRoleService;
    public EscrituraUser setRoleAsUser(EscrituraUser user) {
        logger.log(Level.INFO, "setRoleAsUser");
        return basicRoleService.setRole(user, Roles.USER);
    }
}
