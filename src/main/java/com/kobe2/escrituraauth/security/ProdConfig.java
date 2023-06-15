package com.kobe2.escrituraauth.security;

import com.kobe2.escrituraauth.services.UnauthenticatedService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-prod.properties")
@Profile("prod")
public class ProdConfig {
    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity https,
            UnauthenticatedService unauthenticatedService,
            JwtService jwtService
    ) throws Exception {
        https.authorizeHttpRequests(auth-> auth
                .requestMatchers("a/**")
                .permitAll());
        https.securityMatcher("u2/**")
                .addFilter(new UsernamePasswordFilter(unauthenticatedService));
        https.securityMatcher("u1/**")
                .addFilter(new OnceTokenFilter(unauthenticatedService));
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
