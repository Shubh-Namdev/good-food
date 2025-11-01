package com.goodfood.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class CustomJwtTokenCustomizerConfig {
    

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        
        return context -> {
            System.out.println("CustomJwtTokenCustomizer invoked for " + context.getTokenType().getValue());

            // Apply only for Access Tokens (not ID tokens or refresh tokens)
            if ("access_token".equals(context.getTokenType().getValue())) {
                Authentication principal = context.getPrincipal();

                Set<String> roles = principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
                System.out.println("🎯 Customizing JWT with roles: " + roles);
                System.out.println("🎯 Customizing JWT with roles: " + roles);
                System.out.println("🎯 Customizing JWT with roles: " + roles);

                // Add custom claim
                context.getClaims().claim("roles", roles);
            }
        };
    }
}
