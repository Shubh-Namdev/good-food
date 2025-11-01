package com.goodfood.auth.password;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.HashMap;
import java.util.Map;

public class PasswordGrantAuthenticationConverter implements AuthenticationConverter {

    @Override
    public OAuth2ResourceOwnerPasswordAuthenticationToken convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!"password".equals(grantType)) {
            return null;
        }

        String username = request.getParameter(OAuth2ParameterNames.USERNAME);
        String password = request.getParameter(OAuth2ParameterNames.PASSWORD);

        // client authenticated earlier in filter chain
        Authentication clientPrincipal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (!(clientPrincipal instanceof OAuth2ClientAuthenticationToken clientAuth)) {
            throw new IllegalArgumentException("Client authentication required");
        }

        var registeredClient = clientAuth.getRegisteredClient();

        Map<String, Object> additionalParameters = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> {
            if (!k.equals(OAuth2ParameterNames.GRANT_TYPE)
                    && !k.equals(OAuth2ParameterNames.USERNAME)
                    && !k.equals(OAuth2ParameterNames.PASSWORD)) {
                additionalParameters.put(k, v[0]);
            }
        });

        return new OAuth2ResourceOwnerPasswordAuthenticationToken(registeredClient, clientPrincipal, username, password, additionalParameters);
    }
}
