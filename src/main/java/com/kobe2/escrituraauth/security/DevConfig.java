package com.kobe2.escrituraauth.security;

import com.kobe2.escrituraauth.services.UnauthenticatedService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-dev.properties")
@Profile("dev")
public class DevConfig {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    @Value("${custom.locs.port}")
    private String port;
    @Bean
    public WebClient webClient() {
        logger.log(Level.INFO, "webClient");
        return WebClient.builder()
                .baseUrl(String.format("http://localhost:%s", this.port))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "SECRETVAR")
                .build();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity https, UnauthenticatedService unauthenticatedService) throws Exception {
        logger.log(Level.INFO, "securityFilterChain");
        https.authorizeHttpRequests(auth-> auth
                .requestMatchers("a/**")
                .permitAll());
        https.securityMatcher("u2/**")
                .addFilter(new UsernamePasswordFilter(unauthenticatedService));
        https.securityMatcher("u1/**")
                .addFilterBefore(new OnceTokenFilter(unauthenticatedService), UsernamePasswordFilter.class);
        https.formLogin(AbstractHttpConfigurer::disable);
        https.httpBasic(AbstractHttpConfigurer::disable);
        https.csrf(AbstractHttpConfigurer::disable);
        https.cors(AbstractHttpConfigurer::disable);
        return https.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("**");
    }
}
