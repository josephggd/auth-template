package com.kobe2.escrituraauth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.RegisteredClaims;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kobe2.escrituraauth.entities.AccessToken;
import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.entities.RefreshToken;
import jakarta.ws.rs.NotAuthorizedException;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class JwtService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());

    private final String ACCESS = "SESSION_A";
    private final String REFRESH = "SESSION_B";
    private String issuer = "SECRETVAR";
    private long millisAdd = 1000 * 60 * 24;
    private static final String SECRET = "zZrq0sZK1yt9RJk51RTJ/jeU6WERbvr8nqKMWQJRX1E=";

    public String extractUserId(String token) {
        logger.info("extractUserId");
        Claim claim = verifyToken(token).getClaim(RegisteredClaims.SUBJECT);
        return claim.asString();
    }
    public String extractAccess(String token) {
        logger.info("extractAccess");
        return extractFromToken(token, ACCESS);
    }
    public String extractRefresh(String token) {
        logger.info("extractRefresh");
        return extractFromToken(token, REFRESH);
    }
    private String extractFromToken(String token, String claimName) {
        logger.info("extractFromToken");
        Claim claim = verifyToken(token).getClaim(claimName);
        return claim.asString();
    }

    private DecodedJWT verifyToken(String token) {
        logger.info("verifyToken");
        Algorithm algorithm =Algorithm.HMAC256(Base64.getDecoder().decode(SECRET));
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            logger.warning(e.getMessage());
            throw new NotAuthorizedException("BAD AUTH");
        }
    }

    public String generateUserToken(
            RefreshToken refreshToken,
            AccessToken accessToken
    ) {
        logger.info("generateUserToken");
        long millis = System.currentTimeMillis();
        Algorithm algorithm =Algorithm.HMAC256(Base64.getDecoder().decode(SECRET));
        return JWT
                .create()
                .withIssuer(issuer)
                .withClaim(REFRESH, refreshToken.getCode().toString())
                .withClaim(ACCESS, accessToken.getCode().toString())
                .withIssuedAt(new Date(millis))
                .withExpiresAt(new Date(millis + millisAdd))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public String generateApiToken(
            EscrituraUser proxyUser
    ) {
        logger.info("generateApiToken");
        long millis = System.currentTimeMillis();
        Algorithm algorithm =Algorithm.HMAC256(Base64.getDecoder().decode(SECRET));
        return JWT
                .create()
                .withIssuer(issuer)
                .withSubject(proxyUser.getId().toString())
                .withIssuedAt(new Date(millis))
                .withExpiresAt(new Date(millis + millisAdd))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }
}
