package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.RefreshToken;
import com.kobe2.escrituraauth.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    public RefreshToken findByCode(UUID uuid) {
        Optional<RefreshToken> abstractToken = refreshTokenRepository.findByCode(uuid);
        if (abstractToken.isPresent()){
            return abstractToken.get();
        } else {
            throw new IllegalArgumentException("CODE NOT FOUND");
        }
    }
    public void revokeToken(RefreshToken token){
        logger.log(Level.FINEST, "revokeToken");
        refreshTokenRepository.delete(token);
    }
    public RefreshToken save(RefreshToken token) {
        return refreshTokenRepository.save(token);
    }
    public RefreshToken cCodeCheck(UUID code) {
        logger.log(Level.FINEST, "cCodeCheck");
        RefreshToken token = this.findByCode(code);
        if (token.isExpired()){
            throw new IllegalArgumentException("CODE IS EXPIRED");
        }
        return token;
    }

}
