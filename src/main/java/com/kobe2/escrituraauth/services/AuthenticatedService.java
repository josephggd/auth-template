package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.dtos.LocationRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticatedService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final WebService webService;
    public Flux<LocationRecord> getLocations() {
        logger.log(Level.FINE, "getLocations");
        return webService.getLocationsViaClient();
    }
}
