package com.kobe2.escrituraauth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kobe2.escrituraauth.entities.AccessToken;
import com.kobe2.escrituraauth.services.AccessTokenService;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AuthTokenFilter implements ContainerRequestFilter {
    private final AccessTokenService accessTokenService;
    private final String claim;

    public UUID parseCodeFromAuthHeader(String authHeaderWithBearer) {
        String authHeaderWithoutBearer = authHeaderWithBearer.split("Bearer ")[1];
        DecodedJWT jwt = decodeJWTFromAuthHeader(authHeaderWithoutBearer);
        String uuidString = jwt.getClaim(this.claim).asString();
        return UUID.fromString(uuidString);
    }
    public DecodedJWT decodeJWTFromAuthHeader(String authHeader){
        return JWT.decode(authHeader);
    }
    public boolean getValidTokenValidity(UUID tokenUUID) {
        AccessToken accessToken = accessTokenService.findByCode(tokenUUID);
        return accessToken.isExpired();
    }
    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String header = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        UUID code = parseCodeFromAuthHeader(header);
        boolean tokenValidity = this.getValidTokenValidity(code);
        if (tokenValidity) {
            throw new NotAuthorizedException("Bearer error=\"invalid_token\"");
        }
    }
}
