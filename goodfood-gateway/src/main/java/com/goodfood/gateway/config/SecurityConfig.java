package com.goodfood.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/auth/**", "/oauth2/**", "/actuator/**", "/actuator/health").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(rs -> rs.jwt(Customizer.withDefaults()));
        
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        
        return http.build();
    }
}
