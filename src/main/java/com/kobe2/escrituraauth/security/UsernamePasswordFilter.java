package com.kobe2.escrituraauth.security;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.services.UnauthenticatedService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@RequiredArgsConstructor
public class UsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {
    private final UnauthenticatedService unauthenticatedService;
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        logger.info("findByEmailAndPw");
        try {
            UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);
            setDetails(request, authRequest);
            return this.getAuthenticationManager()
                    .authenticate(authRequest);
        } catch (UserPrincipalNotFoundException userPrincipalNotFoundException) {
            logger.warn("UserPrincipalNotFoundException");
            throw new IllegalArgumentException("bad auth");
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new IllegalArgumentException("bad auth");
        }
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(
            HttpServletRequest request) throws UserPrincipalNotFoundException {
        String username = this.obtainUsername(request);
        String unhashedPw = this.obtainPassword(request);
        EscrituraUser user = unauthenticatedService.loginUser(username, unhashedPw);
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    }
}
