package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.dtos.LocationRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticatedService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final WebClient client;
    public Flux<LocationRecord> getLocationsViaClient() {
        logger.log(Level.FINE, "getLocationsViaClient");
        return client.get()
                .uri("l/all")
                .retrieve()
                .bodyToFlux(LocationRecord.class);
    }
}
