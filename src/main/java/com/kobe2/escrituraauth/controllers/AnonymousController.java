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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("a")
public class AnonymousController {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final UnauthenticatedService unauthenticatedService;
    @PostMapping("/sign/r")
    public ResponseEntity<String> signupRequest(
            @RequestBody UserRecord userRecord
    ) {
        try {
            unauthenticatedService.signupSendConfirmation(userRecord);
            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (Exception e) {
            logger.warning(e.getMessage());
            throw new CannedStatementException();
        }
    }
    @PostMapping("/sign/c/{code}")
    public ResponseEntity<String> signupConfirm(
            @PathVariable UUID code
    ) {
        try {
            unauthenticatedService.signupConfirmUser(code);
            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (Exception e) {
            logger.warning(e.getMessage());
            throw new CannedStatementException();
        }
    }
    @PostMapping("/req/r")
    public ResponseEntity<String> resetRequest(
            @RequestBody UserRecord userRecord
    ) {
        try {
            unauthenticatedService.resetRequest(userRecord);
            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (Exception e) {
            logger.warning(e.getMessage());
            throw new CannedStatementException();
        }
    }
    @PostMapping("/req/c/{code}")
    public ResponseEntity<String> resetConfirm(
            @PathVariable UUID code,
            @RequestBody UserRecord userRecord
    ) {
        try {
            unauthenticatedService.resetConfirm(code, userRecord.password());
            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (Exception e) {
            logger.warning(e.getMessage());
            throw new CannedStatementException();
        }
    }
}
