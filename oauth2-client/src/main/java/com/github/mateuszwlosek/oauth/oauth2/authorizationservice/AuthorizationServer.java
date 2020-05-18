package com.github.mateuszwlosek.oauth.oauth2.authorizationservice;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;


@FeignClient(name = "authorizationServer", url = "${application.authorization-server.url}", configuration = AuthorizationServer.Configuration.class)
public interface AuthorizationServer {

	@PostMapping(value = "${application.authorization-server.token-endpoint}", consumes = APPLICATION_FORM_URLENCODED_VALUE)
	RefreshTokenResponse refreshToken(@RequestBody Map<String, ?> params);

	/**
	 * Required to make Feign use x-www-form-urlencoded
	 */
	class Configuration {
		@Bean
		private Encoder feignFormEncoder(final ObjectFactory<HttpMessageConverters> converters) {
			return new SpringFormEncoder(new SpringEncoder(converters));
		}
	}
}
