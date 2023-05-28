package com.kobe2.escrituraauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class EscrituraAuthApplication {
    @Value("server.port")
    private String port;
    @Bean
    public PasswordEncoder encoder() { return new BCryptPasswordEncoder(); }
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(String.format("http://localhost:%s", port))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "SECRETVAR")
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(EscrituraAuthApplication.class, args);
    }

}
