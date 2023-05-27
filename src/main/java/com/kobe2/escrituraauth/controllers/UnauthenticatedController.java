package com.kobe2.escrituraauth.controllers;

import com.kobe2.escrituraauth.exceptions.CannedStatementException;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.services.UnauthenticatedService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/p")
@RequiredArgsConstructor
public class UnauthenticatedController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().toString());
    private final UnauthenticatedService unauthenticatedService;
    @PostMapping("/sRequest")
    public ResponseEntity<String> sRequest(
            @RequestBody UserRecord userRecord
    ) {
        try {
            String email = unauthenticatedService.signupSendConfirmation(userRecord);
            return new ResponseEntity<>(email, HttpStatus.OK);
        }  catch (Exception e) {
            logger.warn(e.getMessage());
            throw new CannedStatementException();
        }
    }
    @PostMapping("/sConfirm/{code}")
    public ResponseEntity<String> sConfirm(
            @PathVariable UUID code
    ) {
        try {
            String email = unauthenticatedService.signupSaveUser(code);
            return new ResponseEntity<>(email, HttpStatus.OK);
        }  catch (Exception e) {
            logger.warn(e.getMessage());
            throw new CannedStatementException();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody UserRecord userRecord
    ) {
        try {
            String email = unauthenticatedService.loginUser(userRecord);
            return new ResponseEntity<>(email, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new CannedStatementException();
        }
    }
}
