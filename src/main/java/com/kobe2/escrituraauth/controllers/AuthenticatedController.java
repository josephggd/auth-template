package com.kobe2.escrituraauth.controllers;

import com.kobe2.escrituraauth.dtos.LocationRecord;
import com.kobe2.escrituraauth.exceptions.CannedStatementException;
import com.kobe2.escrituraauth.services.AuthenticatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("u1")
public class AuthenticatedController {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final AuthenticatedService authenticatedService;
    @GetMapping("/l")
    public Flux<LocationRecord> getLocations() {
        try {
            return authenticatedService.getLocations();
        }  catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
            throw new CannedStatementException();
        }
    }
}
