package com.github.mateuszwlosek.oauth2.token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;

@Getter
@Builder
@Document
public class RefreshedTokenData {

    @Id
    private String id;

    private final String accessToken;
    private final String refreshToken;
    private final Instant expirationTime;
    private final String sessionId;

    @CreatedDate
    @Indexed(expireAfter = "3d")
    private Date createdDate;
}
