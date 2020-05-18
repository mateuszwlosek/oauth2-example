package com.github.mateuszwlosek.oauth.oauth2.authorizationservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RefreshTokenResponse {

	@JsonProperty("access_token")
	private final String accessToken;

	@JsonProperty("refresh_token")
	private final String refreshToken;

	@JsonProperty("expires_in")
	private final String expiresIn;
}
