package com.kobe2.escrituraauth.feign;

import feign.Feign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("server.port")
    private String port;
    @Bean
    public Client locationClient() {
        return Feign
                .builder()
                .target(
                        Client.class,
                        String.format("http:://localhost:%s", port)
                );
    }
}
