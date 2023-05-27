package com.kobe2.escrituraauth.feign;

import com.kobe2.escrituraauth.dtos.LocationRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@FeignClient("locationClient")
public interface Client {
    @PostMapping("/locations")
    ResponseEntity<Map<String, List<LocationRecord>>> getLocations(
            @Header("Authorization") String authHeader
    );
}
