package com.github.mateuszwlosek.oauth.config;

import com.github.mateuszwlosek.oauth.Controller;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.cors()
				.and()
				.authorizeRequests()
				.mvcMatchers(Controller.PATH + "/*")
				.hasAuthority("SCOPE_test-scope")
				.anyRequest()
				.authenticated()
				.and()
				.oauth2ResourceServer()
				.jwt();
	}
}