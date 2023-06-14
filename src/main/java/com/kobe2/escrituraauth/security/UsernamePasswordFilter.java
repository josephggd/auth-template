package com.kobe2.escrituraauth.security;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.records.UserRecord;
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
public class UsernamePasswordFilter extends OncePerRequestFilter {
    private final List<String> excludeUrlPatterns = List.of("/h2-console", "/a/", "/u1/");
    private final UnauthenticatedService unauthenticatedService;
    public UsernamePasswordFilter(UnauthenticatedService unauthenticatedService) {
        this.unauthenticatedService = unauthenticatedService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        logger.info("shouldNotFilter");
        System.out.println(request.getServletPath());
        return excludeUrlPatterns.stream().anyMatch(s -> request.getServletPath().startsWith(s));
    }
//
//    public UserRecord parseFromRequest(HttpServletRequest request) throws IOException {
//        byte[] inputBytes = StreamUtils.copyToByteArray(request.getInputStream());
//        return new ObjectMapper().readValue(inputBytes, UserRecord.class);
//
//    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("doFilterInternal");
        RequestWrapper wrapper = new RequestWrapper(request);
        UserRecord userRecord = wrapper.toUserRecord();
        EscrituraUser user = unauthenticatedService.loginUser(userRecord.username(), userRecord.password());
        HttpServletResponse newResponse = unauthenticatedService.setHeaders(user, response);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, user.getRoles());
        System.out.println("111111111111111111");
        token.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(wrapper)
        );
        System.out.println("222222222222222222");
        SecurityContextHolder.getContext().setAuthentication(token);
        System.out.println("33333333333333333333333");
        filterChain.doFilter(wrapper, newResponse);
    }

}
