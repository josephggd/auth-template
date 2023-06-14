package com.kobe2.escrituraauth.controllers;

import com.kobe2.escrituraauth.dtos.LocationRecord;
import com.kobe2.escrituraauth.exceptions.CannedStatementException;
import com.kobe2.escrituraauth.services.AuthenticatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("u1")
public class AuthenticatedController {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final AuthenticatedService authenticatedService;
    @GetMapping("/l")
    public ResponseEntity<Flux<LocationRecord>> getLocations() {
        try {
            Flux<LocationRecord> userData = authenticatedService.getLocations();
            return ResponseEntity
                    .ok()
                    .body(userData);
        }  catch (Exception e) {
            logger.warning(e.getMessage());
            throw new CannedStatementException();
        }
    }
}
