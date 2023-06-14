package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.repositories.UserRepository;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class BasicUserService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final UserRepository userRepository;
    public EscrituraUser findByEmail(String email) throws UsernameNotFoundException {
        logger.info("findByEmail");
        Optional<EscrituraUser> byEmail = userRepository.findByUsername(email);
        if (byEmail.isEmpty()) {
            logger.warning("EMAIL NOT FOUND");
            throw new NotAuthorizedException("BAD AUTH");
        }
        return byEmail.get();
    }
    public void save(EscrituraUser escrituraUser) {
        logger.info("save");
        userRepository.save(escrituraUser);
    }
}
