package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.ConfirmationToken;
import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UnauthenticatedService implements UserDetailsService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final UnauthenticatedRoleService unauthenticatedRoleService;
    private final BasicUserService basicUserService;
    private final UserRepository userRepository;
    private final MqProducer mqProducer;
    public void signupSendConfirmation(UserRecord userRecord) throws UsernameNotFoundException {
        logger.log(Level.FINEST, "signupSendConfirmation");
        String email = userRecord.email();
        String encodedPw = passwordEncoder.encode(userRecord.pw());
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
    public void loginUser(UserRecord userRecord) throws UserPrincipalNotFoundException {
        logger.log(Level.FINEST, "findByEmailAndPw");
        String email = userRecord.email();
        String rawPw = userRecord.pw();
        String encodedPw = passwordEncoder.encode(rawPw);
        Optional<EscrituraUser> optional = userRepository.findByEmailAndPw(email, encodedPw);
        if (optional.isPresent()) {
            EscrituraUser user = optional.get();
            if (!user.isConfirmed()){
                throw new IllegalArgumentException("USER NOT CONFIRMED");
            }
        } else {
            basicUserService.findByEmail(email);
            Optional<EscrituraUser> byPw = userRepository.findByPw(encodedPw);
            if (byPw.isEmpty()) {
                throw new UserPrincipalNotFoundException("MISMATCHED CREDS");
            }
            throw new IllegalArgumentException("LOGIN ISSUE");
        }
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EscrituraUser user = basicUserService.findByEmail(username);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPw(), user.getRoles());
    }
}
