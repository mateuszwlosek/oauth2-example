package com.github.mateuszwlosek.oauth.oauth2.token;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshedTokenRepository extends MongoRepository<RefreshedTokenData, String> {

    Optional<RefreshedTokenData> findFirstBySessionIdOrderByCreatedDate(String sessionId);
}
