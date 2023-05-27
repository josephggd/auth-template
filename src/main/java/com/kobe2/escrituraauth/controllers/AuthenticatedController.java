package com.kobe2.escrituraauth.controllers;

import com.kobe2.escrituraauth.dtos.LocationRecord;
import com.kobe2.escrituraauth.exceptions.CannedStatementException;
import com.kobe2.escrituraauth.services.AuthenticatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/api/l")
@RequiredArgsConstructor
public class AuthenticatedController {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final AuthenticatedService authenticatedService;
    @GetMapping("/locs")
    public ResponseEntity<Map<String, List<LocationRecord>>> getLocations(
//            @Header AUTHHEADER SHOULD CONTAIN SESSION TOKEN
    ) {
        try {
            String email = "email";
            Map<String, List<LocationRecord>> locations = authenticatedService.getLocationsViaClient(email);
            return new ResponseEntity<>(locations, HttpStatus.OK);
        }  catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new CannedStatementException();
        }
    }
}
