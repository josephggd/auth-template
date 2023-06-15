package com.kobe2.escrituraauth.client;

import com.kobe2.escrituraauth.records.LocationRecord;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Flux;

public interface LocationClient {
    @GetExchange("/all")
    Flux<LocationRecord> getAllLocations();
    @GetExchange("/l/{lat},{lon}")
    Flux<LocationRecord> getLocationsByLocation(
            @PathVariable("lat") float lat,
            @PathVariable("lon") float lon
    );
    @PostExchange("/n")
    void postNewLocation(@RequestBody LocationRecord locationRecord);
}
