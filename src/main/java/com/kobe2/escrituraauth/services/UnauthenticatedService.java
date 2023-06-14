package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.AccessToken;
import com.kobe2.escrituraauth.entities.ConfirmationToken;
import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.entities.RefreshToken;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.repositories.UserRepository;
import com.kobe2.escrituraauth.security.JwtService;
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

import java.time.LocalDateTime;
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
    private final JwtService jwtService;
//    private final MqProducer mqProducer;
    public void signupSendConfirmation(UserRecord userRecord) throws UsernameNotFoundException {
        logger.info("signupSendConfirmation");
        String email = userRecord.username();
        String encodedPw = passwordEncoder.encode(userRecord.password());
        EscrituraUser unconfirmedUser;
        try {
            unconfirmedUser = basicUserService.findByEmail(email);
        } catch (NotAuthorizedException e) {
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
        EscrituraUser user = unauthenticatedRoleService.setRoleAsUser(requestingUser);
        basicUserService.save(user);
        token.setExpiration(LocalDateTime.MIN);
        confirmationTokenService.save(token);
    }
    public EscrituraUser loginUser(String username, String password) throws NotAuthorizedException {
        logger.log(Level.INFO, "loginUser");
        EscrituraUser user = basicUserService.findByEmail(username);
        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        System.out.println("passwordMatches:"+passwordMatches);
        if (passwordMatches && user.isEnabled()) {
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
        RefreshToken refreshToken = new RefreshToken(user);
        refreshTokenService.save(refreshToken);
        AccessToken accessToken = new AccessToken(user);
        accessTokenService.save(accessToken);
        String jwt = jwtService.generateUserToken(refreshToken, accessToken);
        response.setHeader("Authorization", "Bearer "+jwt);
        return response;
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
            refreshToken.extend();
            refreshTokenService.save(refreshToken);
            return refreshToken.getUser();
        }
    }
    public AccessToken getAccessTokenFromRequest(HttpServletRequest request) {
        logger.info("getAccessTokenFromRequest");
        String accessString = request.getHeader(ACCESS);
        UUID accessUUID = UUID.fromString(accessString);
        return accessTokenService.findByCode(accessUUID);
    }
    public HttpServletResponse checkOrRefreshAccess(RefreshToken refreshToken, AccessToken accessToken, HttpServletResponse response) {
        logger.info("checkOrRefreshAccess");
        EscrituraUser accessUser = accessToken.getUser();
        EscrituraUser refreshUser = refreshToken.getUser();
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
//        try {
            String token = request.getHeader("Authorization").split("Bearer ")[1];
            System.out.println("token:"+token);
            UUID refreshTokenId = UUID.fromString(jwtService.extractRefresh(token));
            RefreshToken refreshToken = refreshTokenService.findByCode(refreshTokenId);
            UUID accessTokenId = UUID.fromString(jwtService.extractAccess(token));
            AccessToken accessToken = accessTokenService.findByCode(accessTokenId);
            return this.checkOrRefreshAccess(refreshToken, accessToken, response);
//        } catch (Exception e) {
//            throw new NotAuthorizedException("MISMATCHED TOKENS");
//        }
    }

    public HttpServletResponse authViaUsernameAndPass(HttpServletRequest request, HttpServletResponse response) throws NotAuthorizedException {
        String username = (String) request.getAttribute("username");
        String password = (String) request.getAttribute("password");
        EscrituraUser user = this.loginUser(username, password);
        HttpServletResponse newResponse = this.setHeaders(user, response);
        return setHeaders(user, newResponse);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return basicUserService.findByEmail(username);
    }
}
