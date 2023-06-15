package com.kobe2.escrituraauth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.logging.Logger;

@Configuration
public class ClientConfig {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    @Value("${custom.locs.port}")
    private Integer port;
    @Bean
    LocationClient locationClient() {
        logger.info("locationClient");
        WebClient client = WebClient.builder()
                .baseUrl(String.format("http://localhost:%s", this.port))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "SECRETVAR")
                .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
        return factory.createClient(LocationClient.class);
    }
}
