package com.kobe2.escrituraauth.security;

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
@ConditionalOnProperty(name="security.enabled", havingValue = "false")
public class DevConfig {
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
