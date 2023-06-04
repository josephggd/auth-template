package com.kobe2.escrituraauth.controllers;

import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.exceptions.CannedStatementException;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.services.UnauthenticatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
public class UnauthenticatedController {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final UnauthenticatedService unauthenticatedService;
    @PostMapping("/u2/l")
    public ResponseEntity<String> login(
            @RequestBody UserRecord userRecord
            ) {
        try {
            EscrituraUser escrituraUser = unauthenticatedService.loginUser(userRecord.username(), userRecord.password());
            HttpHeaders headers = unauthenticatedService.setHeaders(escrituraUser);
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .build();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new CannedStatementException();
        }
    }
}
