package com.kobe2.escrituraauth.controllers;

import com.kobe2.escrituraauth.exceptions.CannedStatementException;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.services.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/api/l")
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final ForgotPasswordService forgotPasswordService;
    @PostMapping("/lChange/{code}")
    public ResponseEntity<String> lChangePass(
            @PathVariable UUID code,
            @RequestBody UserRecord userRecord
    ) {
        try {
            forgotPasswordService.loginResetForgottenPass(code, userRecord);
            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new CannedStatementException();
        }
    }
}
