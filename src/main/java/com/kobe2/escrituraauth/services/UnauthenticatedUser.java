package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.ConfirmationCode;
import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.repositories.CodeRepository;
import com.kobe2.escrituraauth.repositories.UserRepository;
import com.kobe2.escrituraauth.rmq.Message;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
    @NonNull
    private final MqProducer mqProducer;
    public EscrituraUser findByEmailAndPw(String email, String pw) throws UserPrincipalNotFoundException {
        logger.log(Level.FINEST, "findByEmailAndPw");
        Optional<EscrituraUser> optional = userRepository.findByEmailAndPw(email, pw);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            Optional<EscrituraUser> byEmail = userRepository.findByEmail(email);
            Optional<EscrituraUser> byPw = userRepository.findByPw(pw);
            if (byEmail.isEmpty()) {
                throw new UsernameNotFoundException("EMAIL NOT FOUND");
            }
            if (byPw.isEmpty()) {
                throw new UserPrincipalNotFoundException("MISMATCHED CREDS");
            }
            EscrituraUser user = byEmail.get();
            if (credCheck(user, email, pw)) {
                if (user.isEnabled()){
                    return user;
                } else {
                    throw new IllegalArgumentException("USER NOT CONFIRMED");
                }
            } else {
                throw new IllegalArgumentException("MISMATCHED CREDS");
            }
        }
    }
    public boolean credCheck (EscrituraUser user, String email, String pw) {
        logger.log(Level.FINEST, "credCheck");
        return user.getEmail().equals(email) && user.getPw().equals(pw);
    }

    public String signupSendConfirmation(UserRecord userRecord) throws UsernameNotFoundException {
        logger.log(Level.FINEST, "signupSendConfirmation");
        String email = userRecord.email();
        Set<ConfirmationCode> codes;
        EscrituraUser unconfirmedUser;
        Optional<EscrituraUser> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            unconfirmedUser = byEmail.get();
            if (unconfirmedUser.isEnabled()){
                throw new UsernameNotFoundException("USER ALREADY CONFIRMED");
            }
            codes = this.expireAllCodes(unconfirmedUser);
        } else {
            unconfirmedUser = new EscrituraUser(email, userRecord.pw());
            codes = unconfirmedUser.getCodes();
        }
        this.sendUserAConfirmationCode(unconfirmedUser, codes);
        return email;
    }
    private void sendUserAConfirmationCode (EscrituraUser user, Set<ConfirmationCode> codes){
        logger.log(Level.FINEST, "sendUserAConfirmationCode");
        ConfirmationCode newConfirmationCode = new ConfirmationCode(user);
        codes.add(newConfirmationCode);
        user.setCodes(codes);
        codeRepository.save(newConfirmationCode);
        userRepository.save(user);
        Message message = new Message(
                user.getEmail(),
                newConfirmationCode.getCode().toString(),
                false
        );
        mqProducer.sendMessage(message);
    }
    private Set<ConfirmationCode> expireAllCodes (EscrituraUser user) {
        logger.log(Level.FINEST, "expireAllCodes");
        Set<ConfirmationCode> allCodesOldAndNew = new HashSet<>();
        Set<ConfirmationCode> codes = user.getCodes();
        if (codes.size()>0){
            for (ConfirmationCode code : codes){
                code.setExpiration(LocalDate.MIN);
                allCodesOldAndNew.add(code);
            }
            codeRepository.saveAll(allCodesOldAndNew);
        }
        return allCodesOldAndNew;
    }

    public String signupSaveUser(UUID emailCode) throws UsernameNotFoundException {
        logger.log(Level.FINEST, "signupSaveUser");
        Optional<ConfirmationCode> optional = codeRepository.findByCode(emailCode);
        if (optional.isPresent()){
            ConfirmationCode cCode = optional.get();
            if (cCode.isExpired()){
                throw new IllegalArgumentException("CODE IS EXPIRED");
            } else {
                UUID userId = cCode.getUser().getId();
                EscrituraUser user = userService.findById(userId);
                user.setEnabled(true);
                EscrituraUser savedUser = userRepository.save(user);
                return savedUser.getEmail();
            }
        } else {
            throw new UsernameNotFoundException("CODE NOT FOUND");
        }
    }
    public String loginUser(UserRecord userRecord) throws UsernameNotFoundException, UserPrincipalNotFoundException {
        logger.log(Level.FINEST, "loginUser");
        EscrituraUser user = findByEmailAndPw(userRecord.email(), userRecord.pw());
        return user.getEmail();
    }
}
