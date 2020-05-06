package com.github.mateuszwlosek.config;

import com.github.mateuszwlosek.Controller;
import com.github.mateuszwlosek.oauth2.header.AuthorizationHeaderStoreOath2RedirectFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthorizationHeaderStoreOath2RedirectFilter redirectFilter;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers(Controller.PATH + "/*")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .addFilterBefore(redirectFilter, OAuth2AuthorizationRequestRedirectFilter.class)
                    .oauth2Login();
    }
}