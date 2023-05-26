package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.ConfirmationCode;
import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.entities.UserRole;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.repositories.CodeRepository;
import com.kobe2.escrituraauth.repositories.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UnauthenticatedUser {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    @NonNull
    private final UserService userService;

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final CodeRepository codeRepository;
    public EscrituraUser findByEmailAndPw(String email, String pw) throws UserPrincipalNotFoundException {
        logger.log(Level.FINEST, "findByEmailAndPw");
        Optional<EscrituraUser> optional = userRepository.findByEmailAndPw(email, pw);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            Optional<EscrituraUser> byEmail = userRepository.findByEmail(email);
            Optional<EscrituraUser> byPw = userRepository.findByPw(pw);
            if (byEmail.isEmpty()) {
                throw new UsernameNotFoundException("INCORRECT E-MAIL");
            }
            if (byPw.isEmpty()) {
                throw new UserPrincipalNotFoundException("INCORRECT PW");
            }
            throw new IllegalArgumentException("LOGIN FAILURE");
        }
    }
    public EscrituraUser signupSendConfirmation(UserRecord userRecord) throws UsernameNotFoundException {
        logger.log(Level.FINEST, "signupSendConfirmation");
        String email = userRecord.email();
        Optional<EscrituraUser> byEmail = userRepository.findByEmail(email);
        // check if their email exists
        // if exists send their code
        // if not exists send error they never signed up
        if (!byEmail.isEmpty()) {
            EscrituraUser unconfirmedUser = byEmail.get();
            // NEW CODE
            // ADD THEIR CODE AND SAVE IT
            // SEND the CODE
        }
        throw new UsernameNotFoundException("USER EXISTS");
    }
    public EscrituraUser signupSaveUser(UUID emailCode) throws UsernameNotFoundException {
        logger.log(Level.FINEST, "signupSaveUser");
        List<ConfirmationCode> codes = codeRepository.findAllByCode(emailCode);
        if (!codes.isEmpty()){
            Optional<ConfirmationCode> optional = codes.stream()
                    .sorted()
                    .toList()
                    .stream()
                    .findFirst();
            if (optional.isPresent()) {
                ConfirmationCode cCode = optional.get();
                if (cCode.isExpired()){
                    throw new IllegalArgumentException("EXPIRED");
                } else {
                    UUID userId = cCode.getUser().getId();
                    return userService.findById(userId);
                }
            }
        }
        throw new UsernameNotFoundException("CODE NOT FOUND");
    }
    public String loginUser(UserRecord userRecord) throws UsernameNotFoundException, UserPrincipalNotFoundException {
        logger.log(Level.FINEST, "loginUser");
        EscrituraUser user = findByEmailAndPw(userRecord.email(), userRecord.pw());
        return user.getEmail();
    }
}
