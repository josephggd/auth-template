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
        Optional<ConfirmationToken> abstractToken = confirmationTokenRepository.findByCode(uuid);
        if (abstractToken.isPresent()){
            return abstractToken.get();
        } else {
            throw new IllegalArgumentException("CODE NOT FOUND");
        }
    }
    public void revokeToken(ConfirmationToken token){
        logger.log(Level.FINEST, "revokeToken");
        confirmationTokenRepository.delete(token);
    }
    public ConfirmationToken save(ConfirmationToken token) {
        return confirmationTokenRepository.save(token);
    }
    public ConfirmationToken cCodeCheck(UUID code) {
        logger.log(Level.FINEST, "cCodeCheck");
        ConfirmationToken token = this.findByCode(code);
        if (token.isExpired()){
            throw new IllegalArgumentException("CODE IS EXPIRED");
        }
        return token;
    }
}
