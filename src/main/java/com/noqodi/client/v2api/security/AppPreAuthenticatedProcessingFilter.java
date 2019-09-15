package com.noqodi.client.v2api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AppPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final String authorizationHeader = "Authorization";
    private static final TokenExtractor tokenExtractor = new BearerTokenExtractor();

    @Autowired
    public AppPreAuthenticatedProcessingFilter(AppAuthenticationManager appAuthenticationManager) {
        this.setAuthenticationManager(appAuthenticationManager);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }
}