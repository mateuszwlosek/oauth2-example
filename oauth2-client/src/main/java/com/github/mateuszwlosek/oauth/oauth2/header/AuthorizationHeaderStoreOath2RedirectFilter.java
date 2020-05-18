package com.github.mateuszwlosek.oauth.oauth2.header;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationHeaderStoreOath2RedirectFilter extends GenericFilterBean {

	private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

	private final RedirectedHeaderRepository repository;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		if (isSessionPresent()) {
			storeSessionHeader(request);
		}

		chain.doFilter(request, response);
	}

	private boolean isSessionPresent() {
		return Objects.nonNull(RequestContextHolder.getRequestAttributes());
	}

	private void storeSessionHeader(final ServletRequest request) {
		final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		final Optional<String> authorizationHeaderOptional = extractAuthorizationHeader(request);

		authorizationHeaderOptional
				.map(s -> RedirectedHeader.builder()
						.sessionId(sessionId)
						.header(authorizationHeaderOptional.get())
						.build())
				.ifPresent(repository::save);
	}

	private Optional<String> extractAuthorizationHeader(final ServletRequest request) {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;

		return Collections
				.list(httpRequest.getHeaderNames())
				.stream()
				.filter(header -> header.equalsIgnoreCase(AUTHORIZATION_HEADER_NAME))
				.map(httpRequest::getHeader)
				.findFirst();
	}
}
