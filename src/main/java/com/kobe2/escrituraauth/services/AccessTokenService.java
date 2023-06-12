package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.AccessToken;
import com.kobe2.escrituraauth.repositories.AccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;
    private final Logger logger = Logger.getLogger(this.getClass().toString());

    public AccessToken findByCode(UUID uuid) {
        Optional<AccessToken> abstractToken = accessTokenRepository.findByCode(uuid);
        if (abstractToken.isPresent()){
            return abstractToken.get();
        } else {
            throw new IllegalArgumentException("CODE NOT FOUND");
        }
    }
    public void revokeToken(AccessToken token){
        logger.log(Level.INFO, "revokeToken");
        accessTokenRepository.delete(token);
    }
    public AccessToken save(AccessToken token) {
        return accessTokenRepository.save(token);
    }
    public AccessToken cCodeCheck(UUID code) {
        logger.log(Level.INFO, "cCodeCheck");
        AccessToken token = this.findByCode(code);
        if (token.isExpired()){
            throw new IllegalArgumentException("CODE IS EXPIRED");
        }
        return token;
    }

}
