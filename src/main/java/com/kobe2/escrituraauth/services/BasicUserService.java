package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.repositories.UserRepository;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class BasicUserService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final UserRepository userRepository;
    public EscrituraUser findById(UUID id){
        logger.info("findById");
        Optional<EscrituraUser> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new NotAuthorizedException("USER ID NOT FOUND");
        }
    }
    public EscrituraUser findByEmail(String email) throws UsernameNotFoundException {
        logger.info("findByEmail");
        Optional<EscrituraUser> byEmail = userRepository.findByUsername(email);
        if (byEmail.isEmpty()) {
            throw new NotAuthorizedException("EMAIL NOT FOUND");
        }
        return byEmail.get();
    }
    public void save(EscrituraUser escrituraUser) {
        logger.info("save");
        userRepository.save(escrituraUser);
    }
}
