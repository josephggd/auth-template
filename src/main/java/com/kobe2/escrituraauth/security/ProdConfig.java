package com.kobe2.escrituraauth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-prod.properties")
@ConditionalOnProperty(name="custom.security.enabled", havingValue = "true")
public class ProdConfig {
    @Value("${server.port}")
    private String port;
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(String.format("http://localhost:%s", this.port))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "SECRETVAR")
                .build();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity https) throws Exception {
        https.authorizeHttpRequests(auth-> auth
                .requestMatchers("/**")
                .permitAll());
        https.formLogin(AbstractHttpConfigurer::disable);
        https.httpBasic(AbstractHttpConfigurer::disable);
        https.csrf(AbstractHttpConfigurer::disable);
        https.cors(AbstractHttpConfigurer::disable);
        return https.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/**");
    }
}
