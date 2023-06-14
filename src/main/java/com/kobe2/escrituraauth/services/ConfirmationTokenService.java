package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.ConfirmationToken;
import com.kobe2.escrituraauth.repositories.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    public ConfirmationToken findByCode(UUID uuid) {
        logger.log(Level.INFO, "findByCode");
        Optional<ConfirmationToken> abstractToken = confirmationTokenRepository.findByCode(uuid);
        if (abstractToken.isPresent()){
            return abstractToken.get();
        } else {
            throw new IllegalArgumentException("CODE NOT FOUND");
        }
    }
    public void revokeByUser(UUID userId){
        logger.log(Level.INFO, "revokeToken");
        confirmationTokenRepository.deleteAllByUserId(userId);
    }
    public void save(ConfirmationToken token) {
        logger.info( "save");
        confirmationTokenRepository.save(token);
    }
    public void delete(ConfirmationToken token) {
        logger.info( "delete");
        confirmationTokenRepository.delete(token);
    }
    public ConfirmationToken cCodeCheck(UUID code) {
        logger.info( "cCodeCheck");
        ConfirmationToken token = this.findByCode(code);
        if (token.isExpired()){
            throw new IllegalArgumentException("CODE IS EXPIRED");
        }
        return token;
    }
}
