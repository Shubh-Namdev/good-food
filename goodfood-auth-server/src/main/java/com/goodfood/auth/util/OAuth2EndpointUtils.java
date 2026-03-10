package com.goodfood.auth.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class OAuth2EndpointUtils {

    private OAuth2EndpointUtils() {
    }

    public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        request.getParameterMap().forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

    public static String getParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    public static void throwError(String errorCode, String parameterName) {
        OAuth2Error error = new OAuth2Error(
                errorCode,
                "OAuth 2.0 parameter: " + parameterName,
                "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2"
        );
        throw new OAuth2AuthenticationException(error);
    }

    public static Map<String, String> decodeFormBody(String body) {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.hasText(body)) {
            return params;
        }
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            try {
                String[] keyValue = pair.split("=", 2);
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                String value = keyValue.length > 1
                        ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name())
                        : "";
                params.put(key, value);
            } catch (UnsupportedEncodingException e) {
                // Should never happen
            }
        }
        return params;
    }
}
