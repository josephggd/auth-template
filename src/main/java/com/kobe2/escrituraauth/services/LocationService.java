package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.client.LocationClient;
import com.kobe2.escrituraauth.records.LocationRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final LocationClient client;
    public Flux<LocationRecord> getAllLocations() {
        logger.info("getAllLocations");
        return client.getAllLocations();
    }
    public Flux<LocationRecord> getLocationsByLocation(float lat, float lon) {
        logger.info("getLocationsByLocation");
        return client.getLocationsByLocation(lat, lon);
    }
    public void postNewLocation(LocationRecord locationRecord) {
        logger.info("postNewLocation");
        client.postNewLocation(locationRecord);
    }
}
