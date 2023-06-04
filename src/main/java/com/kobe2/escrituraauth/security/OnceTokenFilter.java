package com.kobe2.escrituraauth.security;

import com.kobe2.escrituraauth.services.UnauthenticatedService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
