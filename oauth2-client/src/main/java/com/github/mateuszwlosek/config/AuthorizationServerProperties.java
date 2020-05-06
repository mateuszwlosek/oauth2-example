package com.github.mateuszwlosek.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.authorization-server")
public class AuthorizationServerProperties {

    private String clientId;
    private String clientSecret;
}
