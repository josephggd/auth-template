package com.kobe2.escrituraauth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.RegisteredClaims;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kobe2.escrituraauth.entities.AccessToken;
import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.entities.RefreshToken;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

//    private final Algorithm algorithm =Algorithm.HMAC256(Base64.getDecoder().decode(SECRET));
    private final String ACCESS = "SESSION_A";
    private final String REFRESH = "SESSION_B";
    private String issuer = "SECRETVAR";
    private long millisAdd = 1000 * 60 * 24;
    private static final String SECRET = "zZrq0sZK1yt9RJk51RTJ/jeU6WERbvr8nqKMWQJRX1E=";

    public String extractUserId(String token) {
        Claim claim = verifyToken(token).getClaim(RegisteredClaims.SUBJECT);
        return claim.asString();
    }
    public String extractAccess(String token) {
        return extractFromToken(token, ACCESS);
    }
    public String extractRefresh(String token) {
        return extractFromToken(token, REFRESH);
    }
    private String extractFromToken(String token, String claimName) {
        Claim claim = verifyToken(token).getClaim(claimName);
        return claim.asString();
    }

    private DecodedJWT verifyToken(String token) {
        Algorithm algorithm =Algorithm.HMAC256(Base64.getDecoder().decode(SECRET));
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        return verifier.verify(token);
//        try {
//            return verifier.verify(token);
//        } catch (JWTVerificationException e) {
//            System.out.println("JWTVE");
//            throw new NotAuthorizedException("BAD AUTH");
//        }
    }

    private static boolean isJWTExpired(DecodedJWT decodedJWT) {
        Date expiresAt = decodedJWT.getExpiresAt();
        return expiresAt.before(new Date());
    }

    public String generateUserToken(
            RefreshToken refreshToken,
            AccessToken accessToken
    ) {
        long millis = System.currentTimeMillis();
        Algorithm algorithm =Algorithm.HMAC256(Base64.getDecoder().decode(SECRET));
        String token = JWT
                .create()
                .withIssuer(issuer)
                .withClaim(REFRESH, refreshToken.getCode().toString())
                .withClaim(ACCESS, accessToken.getCode().toString())
                .withIssuedAt(new Date(millis))
                .withExpiresAt(new Date(millis + millisAdd))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
        System.out.println("TOKEN:"+token);
        return token;
    }

    public String generateApiToken(
            EscrituraUser proxyUser
    ) {
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
