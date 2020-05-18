package com.github.mateuszwlosek.oauth.oauth2;

import com.github.mateuszwlosek.oauth.oauth2.resourceservice.ResourceServerHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "oauth2")
public class Oauth2Controller {

    private final Oauth2RedirectionService redirectionHandler;
    private final ResourceServerHandler resourceServerHandler;

    @GetMapping("/test")
    public String oauth2Endpoint(final OAuth2AuthenticationToken token) {
        final Optional<String> authorizationHeader = redirectionHandler.getAuthorizationHeader(token);
        authorizationHeader.ifPresent(header -> log.info("Authorization header: {}", header));

        return resourceServerHandler.requestResourceServer(token);
    }
}
