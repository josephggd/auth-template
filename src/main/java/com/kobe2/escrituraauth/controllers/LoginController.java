package com.kobe2.escrituraauth.controllers;

import com.kobe2.escrituraauth.exceptions.CannedStatementException;
import com.kobe2.escrituraauth.records.LocationRecord;
import com.kobe2.escrituraauth.records.UserRecord;
import com.kobe2.escrituraauth.services.AuthenticatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("u2")
public class LoginController {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final AuthenticatedService authenticatedService;
    @PostMapping("/l")
    public ResponseEntity<Flux<LocationRecord>> login(
            @RequestBody UserRecord userRecord
            ) {
        try {
            Flux<LocationRecord> userData = authenticatedService.getLocationsByUser(userRecord);
            return ResponseEntity
                    .ok()
                    .body(userData);
        } catch (Exception e) {
            logger.warning(e.getMessage());
            throw new CannedStatementException();
        }
    }
}
