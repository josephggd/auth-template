package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final UserRepository userRepository;
    private final AuthenticationCodeService authenticationCodeService;
    private final PasswordEncoder passwordEncoder;
    public void loginResetForgottenPass(UUID emailCode, UserRecord userRecord) {
        logger.log(Level.FINEST, "loginResetForgottenPass");
        EscrituraUser requestingUser = authenticationCodeService.cCodeCheck(emailCode);
        String newEncodedPass = passwordEncoder.encode(userRecord.pw());
        requestingUser.setPw(newEncodedPass);
        userRepository.save(requestingUser);
    }
}
