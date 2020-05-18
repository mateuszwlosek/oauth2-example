package com.github.mateuszwlosek.oauth.oauth2;

import com.github.mateuszwlosek.oauth.oauth2.header.RedirectedHeader;
import com.github.mateuszwlosek.oauth.oauth2.header.RedirectedHeaderRepository;
import com.github.mateuszwlosek.oauth.oauth2.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Oauth2RedirectionService {

	private static final int TOKEN_EXPIRATION_BUFFER_SECONDS = 15;
	private static final String BEARER_ACCESS_TOKEN_PREFIX = "Bearer ";

	private final RefreshTokenService refreshTokenService;
	private final OAuth2AuthorizedClientService authorizedClientService;
	private final RedirectedHeaderRepository redirectedHeaderRepository;

	public Optional<String> getAuthorizationHeader(final OAuth2AuthenticationToken token) {
		final WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
		final String sessionId = details.getSessionId();

		return redirectedHeaderRepository
				.findFirstBySessionIdOrderByCreatedDateDesc(sessionId)
				.map(RedirectedHeader::getHeader);
	}

	public String getBearerAccessToken(final OAuth2AuthenticationToken token) {
		return BEARER_ACCESS_TOKEN_PREFIX + getAccessToken(token);
	}

	public String getAccessToken(final OAuth2AuthenticationToken token) {
		final OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.
				loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName());

		final OAuth2AccessToken accessToken = oAuth2AuthorizedClient.getAccessToken();

		if (hasTokenExpired(accessToken)) {
			return refreshTokenService.getRefreshedAccessToken(token);
		}

		return accessToken.getTokenValue();
	}

	private boolean hasTokenExpired(final OAuth2AccessToken accessToken) {
		if (Objects.isNull(accessToken.getExpiresAt())) {
			return true;
		}

		return accessToken.getExpiresAt().isBefore(Instant.now().minus(TOKEN_EXPIRATION_BUFFER_SECONDS, ChronoUnit.SECONDS));
	}
}
