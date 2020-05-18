package com.github.mateuszwlosek.oauth.oauth2.header;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Builder
@Document
public class RedirectedHeader {

	@Id
	private String id;

	private final String header;
	private final String sessionId;

	@CreatedDate
	@Indexed(expireAfter = "3d")
	private Date createdDate;
}
