package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.AccessToken;
import com.kobe2.escrituraauth.entities.ConfirmationToken;
import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.entities.RefreshToken;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UnauthenticatedService implements UserDetailsService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final String ACCESS = "SESSION_A";
    private final String REFRESH = "SESSION_B";
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final UnauthenticatedRoleService unauthenticatedRoleService;
    private final BasicUserService basicUserService;
    private final UserRepository userRepository;
//    private final MqProducer mqProducer;
    public void signupSendConfirmation(UserRecord userRecord) throws UsernameNotFoundException {
        logger.info("signupSendConfirmation");
        String email = userRecord.username();
        String encodedPw = passwordEncoder.encode(userRecord.password());
        EscrituraUser unconfirmedUser;
        try {
            unconfirmedUser = basicUserService.findByEmail(email);
            if (unconfirmedUser.isConfirmed()){
                throw new IllegalArgumentException("USER ALREADY CONFIRMED");
            }
        } catch (UsernameNotFoundException e) {
            unconfirmedUser = new EscrituraUser(email, encodedPw);
        }
        userRepository.save(unconfirmedUser);
        this.addNewToken(unconfirmedUser);
//        mqProducer.sendConfirmationCode(unconfirmedUser, confirmationToken);
    }
    public void addNewToken(EscrituraUser user) {
        try {
            confirmationTokenService.revokeByUser(user.getId());
        } catch (Exception e) {
            logger.log(Level.INFO, "NO CONFIRMATION CODE");
        }
        ConfirmationToken newToken = new ConfirmationToken(user);
        System.out.println(newToken.getCode());
        confirmationTokenService.save(newToken);
    }
    public void signupConfirmUser(UUID emailCode) throws UsernameNotFoundException {
        logger.log(Level.INFO, "signupSaveUser");
        ConfirmationToken token = confirmationTokenService.cCodeCheck(emailCode);
        EscrituraUser requestingUser = token.getUser();
        unauthenticatedRoleService.setRoleAsUser(requestingUser);
        confirmationTokenService.delete(token);
    }
    public EscrituraUser loginUser(String username, String password) throws NotAuthorizedException {
        logger.log(Level.INFO, "findByEmailAndPw");
        EscrituraUser user = basicUserService.findByEmail(username);
        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        if (passwordMatches && user.isConfirmed()) {
            return user;
        }
        throw new NotAuthorizedException("INCORRECT PW");
    }
    public HttpHeaders setHeaders(EscrituraUser user) {
        logger.log(Level.INFO, "setHeaders");
        AccessToken accessToken = new AccessToken(user);
        accessTokenService.save(accessToken);
        RefreshToken refreshToken = new RefreshToken(user);
        refreshTokenService.save(refreshToken);
        HttpHeaders newSessionHeaders = new HttpHeaders();
        newSessionHeaders.set(REFRESH, refreshToken.getCode().toString());
        newSessionHeaders.set(ACCESS, accessToken.getCode().toString());
        return newSessionHeaders;
    }
    public HttpServletResponse setHeaders(EscrituraUser user, HttpServletResponse response) {
        logger.log(Level.INFO, "setHeaders");
        HttpServletResponse respWithAccess = this.setAccess(user, response);
        return this.setRefresh(user, respWithAccess);
    }
    public HttpServletResponse setRefresh(EscrituraUser user, HttpServletResponse response) {
        logger.log(Level.INFO, "setRefresh");
        RefreshToken refreshToken = new RefreshToken(user);
        refreshTokenService.save(refreshToken);
        response.setHeader(REFRESH, refreshToken.getCode().toString());
        return response;
    }
    public HttpServletResponse setAccess(EscrituraUser user, HttpServletResponse response) {
        logger.log(Level.INFO, "setAccess");
        AccessToken accessToken = new AccessToken(user);
        accessTokenService.save(accessToken);
        response.setHeader(ACCESS, accessToken.getCode().toString());
        return response;
    }
    public EscrituraUser getFromRefreshToken(HttpServletRequest request) {
        logger.info("getFromRefreshToken");
        String refreshString = request.getHeader(REFRESH);
        UUID refreshUUID = UUID.fromString(refreshString);
        RefreshToken refreshToken = refreshTokenService.findByCode(refreshUUID);
        if (refreshToken.isExpired()) {
            throw new NotAuthorizedException("BAD REFRESH");
        } else {
            return refreshToken.getUser();
        }
    }
    public AccessToken getAccessTokenFromRequest(HttpServletRequest request) {
        logger.info("getAccessTokenFromRequest");
        String accessString = request.getHeader(ACCESS);
        UUID accessUUID = UUID.fromString(accessString);
        return accessTokenService.findByCode(accessUUID);
    }
    public HttpServletResponse checkOrRefreshAccess(HttpServletRequest request, HttpServletResponse response, EscrituraUser refreshUser) {
        logger.info("checkOrRefreshAccess");
        AccessToken accessToken = this.getAccessTokenFromRequest(request);
        EscrituraUser accessUser = accessToken.getUser();
        if (accessUser.getId().equals(refreshUser.getId())) {
            if (accessToken.isExpired()) {
                AccessToken newToken = new AccessToken(accessUser);
                accessTokenService.save(newToken);
                response.setHeader(ACCESS, newToken.getCode().toString());
            }
            return response;
        } else {
            throw new NotAuthorizedException("MISMATCHED TOKENS");
        }
    }

    public HttpServletResponse authViaHeaders(HttpServletRequest request, HttpServletResponse response) {
        logger.log(Level.INFO, "checkOrRefreshHeaders");
        try {
            EscrituraUser refreshUser = this.getFromRefreshToken(request);
            return this.checkOrRefreshAccess(request, response, refreshUser);
        } catch (Exception e) {
            throw new NotAuthorizedException("MISMATCHED TOKENS");
        }
    }

    public EscrituraUser authViaUsernameAndPass(HttpServletRequest request, HttpServletResponse response) throws NotAuthorizedException {
        String username = (String) request.getAttribute("username");
        String password = (String) request.getAttribute("password");
        return this.loginUser(username, password);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EscrituraUser user = basicUserService.findByEmail(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles());
    }
}
