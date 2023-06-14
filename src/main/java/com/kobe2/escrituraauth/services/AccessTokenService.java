package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.AccessToken;
import com.kobe2.escrituraauth.repositories.AccessTokenRepository;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;
    private final Logger logger = Logger.getLogger(this.getClass().toString());

    public AccessToken findByCode(UUID uuid) {
        logger.info("findByCode");
        Optional<AccessToken> abstractToken = accessTokenRepository.findByCode(uuid);
        if (abstractToken.isPresent()){
            return abstractToken.get();
        } else {
            logger.warning("CODE NOT FOUND");
            throw new NotAuthorizedException("BAD AUTH");
        }
    }
    public void save(AccessToken token) {
        logger.info("save");
        accessTokenRepository.save(token);
    }

}
