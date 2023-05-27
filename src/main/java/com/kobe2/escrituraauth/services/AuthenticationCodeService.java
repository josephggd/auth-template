package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.entities.AuthenticationCode;
import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.enums.CodePurpose;
import com.kobe2.escrituraauth.repositories.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticationCodeService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final CodeRepository codeRepository;
    private Set<AuthenticationCode> expireAllCodes (EscrituraUser user) {
        logger.log(Level.FINEST, "expireAllCodes");
        Set<AuthenticationCode> allCodesOldAndNew = new HashSet<>();
        Set<AuthenticationCode> codes = user.getCodes();
        if (!codes.isEmpty()){
            for (AuthenticationCode code : codes){
                code.setExpiration(LocalDate.MIN);
                allCodesOldAndNew.add(code);
            }
            codeRepository.saveAll(allCodesOldAndNew);
        }
        return allCodesOldAndNew;
    }
    public EscrituraUser addNewCode(EscrituraUser user, CodePurpose purpose){
        logger.log(Level.FINEST, "addNewCode");
        Set<AuthenticationCode> codes = this.expireAllCodes(user);
        AuthenticationCode newAuthenticationCode = new AuthenticationCode(purpose, user);
        codes.add(newAuthenticationCode);
        user.setCodes(codes);
        return user;
    }

    public EscrituraUser cCodeCheck(UUID emailCode) {
        logger.log(Level.FINEST, "cCodeCheck");
        Optional<AuthenticationCode> optional = codeRepository.findByCode(emailCode);
        if (optional.isPresent()){
            AuthenticationCode cCode = optional.get();
            if (cCode.isExpired()){
                throw new IllegalArgumentException("CODE IS EXPIRED");
            } else {
                return cCode.getUser();
            }
        } else {
            throw new UsernameNotFoundException("CODE NOT FOUND");
        }
    }


}
