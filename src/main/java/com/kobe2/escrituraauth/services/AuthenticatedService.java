package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.dtos.LocationRecord;
import com.kobe2.escrituraauth.feign.Client;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticatedService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    @NonNull
    private final Client client;
    public Map<String, List<LocationRecord>> getLocationsViaClient(
        String email
    ) {
        logger.log(Level.FINE, "getLocationsViaClient");
        ResponseEntity<Map<String, List<LocationRecord>>> responseEntity = client.getLocations(email);
        return responseEntity.getBody();
    }
}
