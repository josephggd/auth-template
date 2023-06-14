package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.dtos.LocationRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticatedService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final WebService webService;
    public Flux<LocationRecord> getLocations() {
        logger.info("getLocations");
        return webService.getLocationsViaClient();
    }
}
