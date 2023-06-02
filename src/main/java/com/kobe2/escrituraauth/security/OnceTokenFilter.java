package com.kobe2.escrituraauth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kobe2.escrituraauth.entities.AccessToken;
import com.kobe2.escrituraauth.services.AccessTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class OnceTokenFilter extends OncePerRequestFilter {
    private final AccessTokenService accessTokenService;
    private final String ACCESS = "SESSION_A";
    private final String REFRESH = "SESSION_R";

    public OnceTokenFilter(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }
    public UUID parseCodeFromAuthHeader(String authHeaderWithBearer) {
        String authHeaderWithoutBearer = authHeaderWithBearer.split("Bearer ")[1];
        DecodedJWT jwt = decodeJWTFromAuthHeader(authHeaderWithoutBearer);
        String uuidString = jwt.getClaim(this.ACCESS).asString();
        return UUID.fromString(uuidString);
    }
    public DecodedJWT decodeJWTFromAuthHeader(String authHeader){
        return JWT.decode(authHeader);
    }
    public void verifyTokenValidity(HttpServletRequest request) {
        // TODO: verify both access/refresh
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        UUID code = parseCodeFromAuthHeader(header);
        AccessToken accessToken = accessTokenService.findByCode(code);
        if (accessToken.isExpired()) {
            throw new NotAuthorizedException("Bearer error=\"invalid_token\"");
        }
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        verifyTokenValidity(request);
        filterChain.doFilter(request, response);
    }
}
