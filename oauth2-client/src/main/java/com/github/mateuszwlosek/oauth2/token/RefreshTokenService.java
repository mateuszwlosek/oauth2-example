package com.github.mateuszwlosek.oauth2.token;

import com.github.mateuszwlosek.config.AuthorizationServerProperties;
import com.github.mateuszwlosek.oauth2.authorizationservice.AuthorizationServer;
import com.github.mateuszwlosek.oauth2.authorizationservice.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final int REFRESH_TOKEN_EXPIRATION_BUFFER_SECONDS = 15;

    private final AuthorizationServer authorizationServer;
    private final RefreshedTokenRepository refreshedTokenRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final AuthorizationServerProperties authorizationServerProperties;

    public String getRefreshedAccessToken(final OAuth2AuthenticationToken token) {
        log.info("Refreshing token");
        final OAuth2AuthorizedClient client = authorizedClientService.
                loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName());

        final OAuth2RefreshToken oAuth2refreshToken = client.getRefreshToken();

        final WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
        final String sessionId = details.getSessionId();

        final String refreshToken;

        final Optional<RefreshedTokenData> refreshedTokenData = refreshedTokenRepository.findFirstBySessionIdOrderByCreatedDate(sessionId);
        if (refreshedTokenData.isPresent()) {

            if (hasRefreshTokenNotExpired(refreshedTokenData.get())) {
                return refreshedTokenData.get().getAccessToken();
            } else {
                refreshToken = refreshedTokenData.get().getRefreshToken();
            }

        } else {
            refreshToken = Optional.ofNullable(oAuth2refreshToken)
                    .map(AbstractOAuth2Token::getTokenValue)
                    .orElseThrow(() -> new IllegalArgumentException("Refresh token has not been fetched from authorization server"));
        }

        log.info("Refr");
        final RefreshTokenResponse refreshTokenResponse = refreshToken(refreshToken, sessionId);
        return refreshTokenResponse.getAccessToken();
    }

    private RefreshTokenResponse refreshToken(final String refreshToken, final String sessionId) {
        final Map<String, String> params = buildRefreshTokenParams(refreshToken);
        final RefreshTokenResponse refreshTokenResponse = authorizationServer.refreshToken(params);
        storeRefreshedTokenData(refreshTokenResponse, sessionId);
        return refreshTokenResponse;
    }

    private void storeRefreshedTokenData(final RefreshTokenResponse refreshTokenResponse, final String sessionId) {
        final Instant expirationTime = Instant.now().plus(Long.parseLong(refreshTokenResponse.getExpiresIn()), ChronoUnit.SECONDS);

        final RefreshedTokenData refreshedTokenData = RefreshedTokenData.builder()
                .accessToken(refreshTokenResponse.getAccessToken())
                .refreshToken(refreshTokenResponse.getRefreshToken())
                .expirationTime(expirationTime)
                .sessionId(sessionId).build();

        refreshedTokenRepository.insert(refreshedTokenData);
    }

    private Map<String, String> buildRefreshTokenParams(final String refreshToken) {
        final Map<String, String > params = new HashMap<>();
        params.put("client_id", authorizationServerProperties.getClientId());
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", refreshToken);
        params.put("client_secret", authorizationServerProperties.getClientSecret());

        return params;
    }

    private boolean hasRefreshTokenNotExpired(final RefreshedTokenData refreshedTokenData) {
        return Instant.now().isBefore(refreshedTokenData.getExpirationTime().minus(REFRESH_TOKEN_EXPIRATION_BUFFER_SECONDS, ChronoUnit.SECONDS));

    }
}
