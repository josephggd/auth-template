package com.kobe2.escrituraauth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kobe2.escrituraauth.entities.AccessToken;
import com.kobe2.escrituraauth.entities.RefreshToken;
import com.kobe2.escrituraauth.services.AccessTokenService;
import com.kobe2.escrituraauth.services.RefreshTokenService;
import com.kobe2.escrituraauth.services.UnauthenticatedService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class OnceTokenFilter extends OncePerRequestFilter {
    private final UnauthenticatedService unauthenticatedService;
    public OnceTokenFilter(UnauthenticatedService unauthenticatedService) {
        this.unauthenticatedService = unauthenticatedService;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponse newResponse = unauthenticatedService.checkOrRefreshHeaders(request, response);
        filterChain.doFilter(request, newResponse);
    }
}
