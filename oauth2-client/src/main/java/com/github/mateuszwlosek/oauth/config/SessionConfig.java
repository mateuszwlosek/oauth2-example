package com.github.mateuszwlosek.oauth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Service
@RequiredArgsConstructor
public class SessionConfig implements HttpSessionListener {

	private final SessionProperties properties;

	@Override
	public void sessionCreated(final HttpSessionEvent event) {
		event.getSession().setMaxInactiveInterval(properties.getMaxInactiveTimeSeconds());
	}

	@Override
	public void sessionDestroyed(final HttpSessionEvent event) {

	}
}