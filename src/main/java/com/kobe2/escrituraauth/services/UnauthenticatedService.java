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
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UnauthenticatedService implements UserDetailsService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private static final String BEARER = "Bearer ";
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

    public void resetRequest(UserRecord userRecord) {
        logger.info("resetRequest");
        String email = userRecord.username();
        EscrituraUser unconfirmedUser = basicUserService.findByEmail(email);
        this.addNewToken(unconfirmedUser);
//        mqProducer.sendConfirmationCode(unconfirmedUser, confirmationToken);
    }
    public void resetConfirm(UUID emailCode, String newPass) throws UsernameNotFoundException {
        logger.info( "resetConfirm");
        ConfirmationToken token = confirmationTokenService.cCodeCheck(emailCode);
        String encodedPw = passwordEncoder.encode(newPass);
        EscrituraUser requestingUser = token.getUser();
        requestingUser.setPassword(encodedPw);
        basicUserService.save(requestingUser);
        token.setExpiration(LocalDateTime.MIN);
        confirmationTokenService.save(token);
    }
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
        logger.info("addNewToken");
        try {
            confirmationTokenService.revokeByUser(user.getId());
        } catch (Exception e) {
            logger.warning("NO CONFIRMATION CODE");
        }
        ConfirmationToken newToken = new ConfirmationToken(user);
        confirmationTokenService.save(newToken);
    }
    public void signupConfirmUser(UUID emailCode) throws UsernameNotFoundException {
        logger.info( "signupSaveUser");
        ConfirmationToken token = confirmationTokenService.cCodeCheck(emailCode);
        EscrituraUser requestingUser = token.getUser();
        EscrituraUser user = unauthenticatedRoleService.setRoleAsUser(requestingUser);
        basicUserService.save(user);
        token.setExpiration(LocalDateTime.MIN);
        confirmationTokenService.save(token);
    }
    public EscrituraUser loginUser(String username, String password) throws NotAuthorizedException {
        logger.info( "loginUser");
        EscrituraUser user = basicUserService.findByEmail(username);
        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        if (passwordMatches && user.isEnabled()) {
            return user;
        }
        logger.warning("INCORRECT PW");
        throw new NotAuthorizedException("BAD AUTH");
    }
    public HttpServletResponse setHeaders(EscrituraUser user, HttpServletResponse response) {
        logger.info( "setHeaders");
        RefreshToken refreshToken = new RefreshToken(user);
        refreshTokenService.save(refreshToken);
        AccessToken accessToken = new AccessToken(user);
        accessTokenService.save(accessToken);
        String jwt = jwtService.generateUserToken(refreshToken, accessToken);
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER +jwt);
        return response;
    }
    public HttpServletResponse checkOrRefreshAccess(RefreshToken refreshToken, AccessToken accessToken, HttpServletResponse response) {
        logger.info("checkOrRefreshAccess");
        EscrituraUser accessUser = accessToken.getUser();
        EscrituraUser refreshUser = refreshToken.getUser();
        if (accessUser.getId().equals(refreshUser.getId())) {
            if (accessToken.isExpired()) {
                AccessToken newToken = new AccessToken(accessUser);
                accessTokenService.save(newToken);
                String jwt = jwtService.generateUserToken(refreshToken, newToken);
                response.setHeader(HttpHeaders.AUTHORIZATION, BEARER +jwt);
            }
            return response;
        } else {
            logger.warning("MISMATCHED TOKENS");
            throw new NotAuthorizedException("BAD AUTH");
        }
    }

    public HttpServletResponse authViaHeaders(HttpServletRequest request, HttpServletResponse response) {
        logger.info( "checkOrRefreshHeaders");
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(BEARER)[1];
            UUID refreshTokenId = UUID.fromString(jwtService.extractRefresh(token));
            RefreshToken refreshToken = refreshTokenService.findByCode(refreshTokenId);
            UUID accessTokenId = UUID.fromString(jwtService.extractAccess(token));
            AccessToken accessToken = accessTokenService.findByCode(accessTokenId);
            return this.checkOrRefreshAccess(refreshToken, accessToken, response);
        } catch (Exception e) {
            logger.warning(e.getMessage());
            throw new NotAuthorizedException("BAD AUTH");
        }
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("loadUserByUsername");
        return basicUserService.findByEmail(username);
    }
}
