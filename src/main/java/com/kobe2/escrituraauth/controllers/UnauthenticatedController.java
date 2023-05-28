package com.kobe2.escrituraauth.controllers;

import com.kobe2.escrituraauth.exceptions.CannedStatementException;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.services.UnauthenticatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
public class UnauthenticatedController {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final UnauthenticatedService unauthenticatedService;
    @PostMapping("/sRequest")
    public ResponseEntity<String> sRequest(
            @RequestBody UserRecord userRecord
    ) {
        try {
            unauthenticatedService.signupSendConfirmation(userRecord);
            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new CannedStatementException();
        }
    }
    @PostMapping("/sConfirm/{code}")
    public ResponseEntity<String> sConfirm(
            @PathVariable UUID code
    ) {
        try {
            unauthenticatedService.signupConfirmUser(code);
            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new CannedStatementException();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody UserRecord userRecord
    ) {
        try {
            unauthenticatedService.loginUser(userRecord);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new CannedStatementException();
        }
    }
}
