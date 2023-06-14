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
import java.util.List;

@Component
public class OnceTokenFilter extends OncePerRequestFilter {
    private final List<String> excludeUrlPatterns = List.of("/h2-console", "/a/", "/u2/");
    private final UnauthenticatedService unauthenticatedService;
    private final JwtService jwtService;
    public OnceTokenFilter(
            UnauthenticatedService unauthenticatedService,
            JwtService jwtService
    ) {
        this.unauthenticatedService = unauthenticatedService;
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        logger.info("shouldNotFilter");
        System.out.println(request.getServletPath());
        return excludeUrlPatterns.stream().anyMatch(s -> request.getServletPath().startsWith(s));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("doFilterInternal");
        HttpServletResponse newResponse = unauthenticatedService.authViaHeaders(request, response);
        filterChain.doFilter(request, newResponse);
    }

}
