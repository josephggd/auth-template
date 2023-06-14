package com.kobe2.escrituraauth.security;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.services.UnauthenticatedService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class OnceTokenFilter extends OncePerRequestFilter {
    private final List<String> excludeUrlPatterns = List.of("/h2-console", "/a/", "/u2/");
    private final UnauthenticatedService unauthenticatedService;
    public OnceTokenFilter(UnauthenticatedService unauthenticatedService) {
        this.unauthenticatedService = unauthenticatedService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        logger.info("shouldNotFilter");
        System.out.println(request.getServletPath());
        return excludeUrlPatterns.stream().anyMatch(s -> request.getServletPath().startsWith(s));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("doFilterInternal");
        HttpServletResponse newResponse;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            newResponse = unauthenticatedService.authViaHeaders(request, response);
            filterChain.doFilter(request, newResponse);
        } else {
            String username = (String) request.getAttribute("username");
            String password = (String) request.getAttribute("password");
            EscrituraUser user = unauthenticatedService.loginUser(username, password);
            newResponse = unauthenticatedService.setHeaders(user, response);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, user.getRoles());
            token.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, newResponse);
    }

}
