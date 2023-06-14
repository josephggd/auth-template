package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.dtos.LocationRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@Service
public class WebService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    @Value("${custom.locs.port}")
    private Integer port;
    private WebClient client;

    public WebService(WebClient.Builder webClientBuilder) {
        this.client = webClientBuilder
                .defaultHeader(HttpHeaders.AUTHORIZATION, "SECRETVAR")
                .build();
    }
    public Flux<LocationRecord> getLocationsViaClient() {
        logger.info("getLocationsViaClient");
        return client.get()
                .uri(String.format("http://localhost:%s", this.port))
                .retrieve()
                .bodyToFlux(LocationRecord.class);
    }
}
