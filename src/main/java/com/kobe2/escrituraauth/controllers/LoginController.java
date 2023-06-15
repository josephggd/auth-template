package com.kobe2.escrituraauth.controllers;

import com.kobe2.escrituraauth.exceptions.CannedStatementException;
import com.kobe2.escrituraauth.records.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("u2")
public class LoginController {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRecord userRecord) {
        try {
            return ResponseEntity
                    .ok()
                    .body(userRecord.username());
        } catch (Exception e) {
            logger.warning(e.getMessage());
            throw new CannedStatementException();
        }
    }
}
