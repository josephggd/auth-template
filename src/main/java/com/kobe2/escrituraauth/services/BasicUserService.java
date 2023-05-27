package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.repositories.UserRepository;
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
        logger.log(Level.FINEST, "findById");
        Optional<EscrituraUser> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new IllegalArgumentException("USER ID NOT FOUND");
        }
    }
    public EscrituraUser findByEmail(String email) {
        Optional<EscrituraUser> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            throw new UsernameNotFoundException("EMAIL NOT FOUND");
        }
        return byEmail.get();
    }
}
