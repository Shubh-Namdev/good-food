package com.goodfood.auth.password;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Map;

public class OAuth2ResourceOwnerPasswordAuthenticationToken extends AbstractAuthenticationToken {

    private final RegisteredClient registeredClient;
    private final Authentication clientPrincipal;
    private final String username;
    private final String password;
    private final Map<String, Object> additionalParameters;

    public OAuth2ResourceOwnerPasswordAuthenticationToken(
            RegisteredClient registeredClient,
            Authentication clientPrincipal,
            String username,
            String password,
            Map<String, Object> additionalParameters) {

        super(null);
        this.registeredClient = registeredClient;
        this.clientPrincipal = clientPrincipal;
        this.username = username;
        this.password = password;
        this.additionalParameters = additionalParameters;
        setAuthenticated(false);
    }

    public RegisteredClient getRegisteredClient() {
        return registeredClient;
    }

    public Authentication getClientPrincipal() {
        return clientPrincipal;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, Object> getAdditionalParameters() {
        return additionalParameters;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
}
