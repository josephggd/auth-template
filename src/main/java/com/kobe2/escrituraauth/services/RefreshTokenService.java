package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.RefreshToken;
import com.kobe2.escrituraauth.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    public RefreshToken findByCode(UUID uuid) {
        logger.info("findByCode");
        Optional<RefreshToken> abstractToken = refreshTokenRepository.findByCode(uuid);
        if (abstractToken.isPresent()){
            return abstractToken.get();
        } else {
            logger.warning("CODE NOT FOUND");
            throw new IllegalArgumentException("BAD AUTH");
        }
    }

    public void save(RefreshToken refreshToken) {
        logger.info("save");
        refreshTokenRepository.save(refreshToken);
    }
}
