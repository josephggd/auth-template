package com.kobe2.escrituraauth.security;

import com.kobe2.escrituraauth.services.UnauthenticatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-dev.properties")
@RequiredArgsConstructor
@Profile("dev")
public class DevConfig {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final AuthenticationProvider authenticationProvider;
    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity https,
            UnauthenticatedService unauthenticatedService,
            JwtService jwtService
    ) throws Exception {
        logger.info( "securityFilterChain");
        https.formLogin(AbstractHttpConfigurer::disable);
        https.httpBasic(AbstractHttpConfigurer::disable);
        https.csrf(AbstractHttpConfigurer::disable);
        https.cors(AbstractHttpConfigurer::disable);
        https.authorizeHttpRequests(auth-> auth
                .requestMatchers("a/**", "/h2-console")
                .permitAll());
        https.securityMatcher("u2/**")
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(new UsernamePasswordFilter(unauthenticatedService), UsernamePasswordAuthenticationFilter.class);
        https.securityMatcher("u1/**")
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(new OnceTokenFilter(unauthenticatedService), UsernamePasswordFilter.class);
        return https.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("**");
    }
}
