package com.kobe2.escrituraauth.controllers;

import com.kobe2.escrituraauth.exceptions.CannedStatementException;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.services.UnauthenticatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("a")
public class AnonymousController {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final UnauthenticatedService unauthenticatedService;
    @PostMapping("/s/r")
    public ResponseEntity<String> signupRequest(
            @RequestBody UserRecord userRecord
    ) {
        try {
            unauthenticatedService.signupSendConfirmation(userRecord);
            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
            throw new CannedStatementException();
        }
    }
    @PostMapping("/s/c/{code}")
    public ResponseEntity<String> signupConfirm(
            @PathVariable UUID code
    ) {
        try {
            unauthenticatedService.signupConfirmUser(code);
            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
            throw new CannedStatementException();
        }
    }
}
