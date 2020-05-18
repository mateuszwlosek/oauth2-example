package com.github.mateuszwlosek.oauth.oauth2.header;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RedirectedHeaderRepository extends MongoRepository<RedirectedHeader, String> {

	Optional<RedirectedHeader> findFirstBySessionIdOrderByCreatedDateDesc(String sessionId);
}
