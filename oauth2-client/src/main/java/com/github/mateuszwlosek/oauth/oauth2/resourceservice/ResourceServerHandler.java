package com.github.mateuszwlosek.oauth.oauth2.resourceservice;

import com.github.mateuszwlosek.oauth.oauth2.Oauth2RedirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceServerHandler {

    private final Oauth2RedirectionService redirectionHandler;
    private final ResourceServer resourceServer;

    public String requestResourceServer(final OAuth2AuthenticationToken token) {
        final String bearerToken = redirectionHandler.getBearerAccessToken(token);
        return resourceServer.request(bearerToken);
    }
}
