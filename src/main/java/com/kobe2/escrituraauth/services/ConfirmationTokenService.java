package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.ConfirmationToken;
import com.kobe2.escrituraauth.entities.EscrituraUser;
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
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final ConfirmationTokenRepository<ConfirmationToken> confirmationTokenRepository;
    public ConfirmationToken findByCode(UUID code){
        logger.log(Level.FINEST, "findByCode");
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByCode(code);
        if (confirmationToken.isPresent()) {
            return confirmationToken.get();
        } else {
            throw new IllegalArgumentException("CODE NOT FOUND");
        }
    }
    public void revokeToken(UUID code){
        logger.log(Level.FINEST, "revokeToken");
        ConfirmationToken confirmationToken = this.findByCode(code);
        confirmationTokenRepository.delete(confirmationToken);
    }
    public void revokeToken(ConfirmationToken confirmationToken){
        logger.log(Level.FINEST, "revokeToken");
        confirmationTokenRepository.delete(confirmationToken);
    }
    public EscrituraUser cCodeCheck(UUID code) {
        logger.log(Level.FINEST, "cCodeCheck");
        ConfirmationToken confirmationToken = this.findByCode(code);
        if (confirmationToken.isExpired()){
            throw new IllegalArgumentException("CODE IS EXPIRED");
        } else {
            return confirmationToken.getUser();
        }
    }
    public ConfirmationToken addNewToken(EscrituraUser user) {
        try {
            ConfirmationToken token = user.getCToken();
            revokeToken(token);
        } catch (Exception e) {
            logger.log(Level.WARNING, "NO CONFIRMATION CODE");
        }
        ConfirmationToken newToken = new ConfirmationToken(user);
        return confirmationTokenRepository.save(newToken);

    }
}
