package com.kobe2.escrituraauth.services;

import com.kobe2.escrituraauth.dtos.LocationRecord;
import com.kobe2.escrituraauth.entities.EscrituraUser;
import com.kobe2.escrituraauth.records.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticatedService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final WebService webService;
    private final BasicUserService basicUserService;
    public Flux<LocationRecord> getLocations() {
        logger.info("getLocations");
        return webService.getLocationsViaClient();
    }
    public Flux<LocationRecord> getLocationsByUser(UserRecord userRecord) {
        logger.info("getLocationsByUser");
        EscrituraUser user = basicUserService.findByEmail(userRecord.username());
        return webService.getLocationsViaClient(user);
    }
}
