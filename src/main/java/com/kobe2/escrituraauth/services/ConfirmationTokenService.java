package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.ConfirmationToken;
import com.kobe2.escrituraauth.repositories.ConfirmationTokenRepository;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    public ConfirmationToken findByCode(UUID uuid) {
        logger.info( "findByCode");
        Optional<ConfirmationToken> abstractToken = confirmationTokenRepository.findByCode(uuid);
        if (abstractToken.isPresent()){
            return abstractToken.get();
        } else {
            logger.warning("CODE NOT FOUND");
            throw new IllegalArgumentException("BAD AUTH");
        }
    }
    public void revokeByUser(UUID userId){
        logger.info( "revokeToken");
        confirmationTokenRepository.deleteAllByUserId(userId);
    }
    public void save(ConfirmationToken token) {
        logger.info( "save");
        confirmationTokenRepository.save(token);
    }
    public ConfirmationToken cCodeCheck(UUID code) {
        logger.info( "cCodeCheck");
        ConfirmationToken token = this.findByCode(code);
        if (token.isExpired()){
            logger.warning("CODE EXPIRED");
            throw new NotAuthorizedException("BAD AUTH");
        }
        return token;
    }
}
