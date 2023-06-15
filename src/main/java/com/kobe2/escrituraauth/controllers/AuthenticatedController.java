package com.kobe2.escrituraauth.controllers;

import com.kobe2.escrituraauth.exceptions.CannedStatementException;
import com.kobe2.escrituraauth.records.LocationRecord;
import com.kobe2.escrituraauth.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("u1")
public class AuthenticatedController {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final LocationService locationService;
    @GetMapping("/all")
    public ResponseEntity<Flux<LocationRecord>> getAllLocations() {
        try {
            Flux<LocationRecord> userData = locationService.getAllLocations();
            return ResponseEntity
                    .ok()
                    .body(userData);
        }  catch (Exception e) {
            logger.warning(e.getMessage());
            throw new CannedStatementException();
        }
    }
    @GetMapping("/loc/{lat},{lon}")
    public ResponseEntity<Flux<LocationRecord>> getLocationsByLocation(
            @PathVariable("lat") float lat,
            @PathVariable("lon") float lon
    ) {
        try {
            Flux<LocationRecord> userData = locationService.getLocationsByLocation(lat, lon);
            return ResponseEntity
                    .ok()
                    .body(userData);
        }  catch (Exception e) {
            logger.warning(e.getMessage());
            throw new CannedStatementException();
        }
    }
    @PostMapping("/new")
    public ResponseEntity<String> postNewLocation(LocationRecord locationRecord) {
        try {
            locationService.postNewLocation(locationRecord);
            return ResponseEntity
                    .ok()
                    .build();
        }  catch (Exception e) {
            logger.warning(e.getMessage());
            throw new CannedStatementException();
        }
    }
}
