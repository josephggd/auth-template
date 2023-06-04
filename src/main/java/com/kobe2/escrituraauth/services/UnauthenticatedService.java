package com.kobe2.escrituraauth.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
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

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Enumeration;
import java.util.Optional;
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
    private final MqProducer mqProducer;
    public void signupSendConfirmation(UserRecord userRecord) throws UsernameNotFoundException {
        logger.log(Level.FINEST, "signupSendConfirmation");
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
        ConfirmationToken confirmationToken = this.addNewToken(unconfirmedUser);
        userRepository.save(unconfirmedUser);
        mqProducer.sendConfirmationCode(unconfirmedUser, confirmationToken);
    }
    public ConfirmationToken addNewToken(EscrituraUser user) {
        try {
            ConfirmationToken token = user.getCToken();
            confirmationTokenService.revokeToken(token);
        } catch (Exception e) {
            logger.log(Level.WARNING, "NO CONFIRMATION CODE");
        }
        ConfirmationToken newToken = new ConfirmationToken(user);
        return confirmationTokenService.save(newToken);
    }
    public void signupConfirmUser(UUID emailCode) throws UsernameNotFoundException {
        logger.log(Level.FINEST, "signupSaveUser");
        ConfirmationToken token = confirmationTokenService.cCodeCheck(emailCode);
        EscrituraUser requestingUser = token.getUser();
        EscrituraUser userWithUserRole = unauthenticatedRoleService.setRoleAsUser(requestingUser);
        userRepository.save(userWithUserRole);
    }
    public EscrituraUser loginUser(String username, String password) throws UserPrincipalNotFoundException {
        logger.log(Level.FINEST, "findByEmailAndPw");
        String encodedPw = passwordEncoder.encode(password);
        Optional<EscrituraUser> optional = userRepository.findByUsernameAndPassword(username, encodedPw);
        if (optional.isPresent()) {
            EscrituraUser user = optional.get();
            if (!user.isConfirmed()){
                throw new IllegalArgumentException("USER NOT CONFIRMED");
            }
            return user;
        } else {
            basicUserService.findByEmail(username);
            Optional<EscrituraUser> byPw = userRepository.findByPassword(password);
            if (byPw.isEmpty()) {
                throw new UserPrincipalNotFoundException("MISMATCHED CREDS");
            }
            throw new IllegalArgumentException("LOGIN ISSUE");
        }
    }
    public HttpHeaders setHeaders(EscrituraUser user) {
        AccessToken accessToken = new AccessToken(user);
        accessTokenService.save(accessToken);
        RefreshToken refreshToken = new RefreshToken(user);
        refreshTokenService.save(refreshToken);
        HttpHeaders newSessionHeaders = new HttpHeaders();
        newSessionHeaders.set(REFRESH, refreshToken.getCode().toString());
        newSessionHeaders.set(ACCESS, accessToken.getCode().toString());
        return newSessionHeaders;
    }
    public HttpServletResponse checkOrRefreshHeaders (HttpServletRequest request, HttpServletResponse response) {
        String refreshString = request.getHeader(REFRESH);
        UUID refreshUUID = UUID.fromString(refreshString);
        RefreshToken refreshToken = refreshTokenService.findByCode(refreshUUID);
        String accessString = request.getHeader(ACCESS);
        UUID accessUUID = UUID.fromString(accessString);
        AccessToken accessToken = accessTokenService.findByCode(accessUUID);

        EscrituraUser refreshUser = refreshToken.getUser();
        EscrituraUser accessUser = accessToken.getUser();

        if (refreshUser.getId().equals(accessUser.getId())) {
            if (refreshToken.isExpired()) {
                throw new NotAuthorizedException("BAD REFRESH");
            }
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                response.setHeader(headerName, request.getHeader(headerName));
            }
            if (accessToken.isExpired()) {
                AccessToken newToken = new AccessToken(accessUser);
                accessTokenService.save(newToken);
                response.setHeader(ACCESS, newToken.getCode().toString());
            }
            return response;
        }
        throw new NotAuthorizedException("MISMATCHED TOKENS");
    }
    public UUID parseSessionToken(String authHeaderWithBearer) {
        String authHeaderWithoutBearer = authHeaderWithBearer.split("Bearer ")[1];
        DecodedJWT jwt = decodeJWTFromAuthHeader(authHeaderWithoutBearer);
        String uuidString = jwt.getClaim(this.ACCESS).asString();
        return UUID.fromString(uuidString);
    }
    public DecodedJWT decodeJWTFromAuthHeader(String authHeader){
        return JWT.decode(authHeader);
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EscrituraUser user = basicUserService.findByEmail(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles());
    }
}
