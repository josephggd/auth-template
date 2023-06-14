package com.kobe2.escrituraauth.security;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.services.UnauthenticatedService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class UsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final UnauthenticatedService unauthenticatedService;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        logger.info("attemptAuthentication");
        UsernamePasswordAuthenticationToken token = this.getAuthRequest(request);
        return this.getAuthenticationManager()
                .authenticate(token);
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(
            HttpServletRequest request
    ) throws NotAuthorizedException {
        logger.info("getAuthRequest");
        String username = this.obtainUsername(request);
        String unhashedPw = this.obtainPassword(request);
        EscrituraUser user = unauthenticatedService.loginUser(username, unhashedPw);
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getRoles());
    }
}
