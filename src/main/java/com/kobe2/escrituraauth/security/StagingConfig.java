package com.kobe2.escrituraauth.security;

import com.kobe2.escrituraauth.enums.Authority;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name="security.enabled", havingValue = "true")
public class StagingConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity https) throws Exception {
        https.authorizeHttpRequests(auth-> auth
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/m/**")
                .hasAuthority(Authority.MAINTAINER.toString())
                .requestMatchers("/p/**")
                .hasAuthority(Authority.USER.toString())
        );
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
