package com.github.mateuszwlosek.oauth.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private static final String JWT_USERNAME_CLAIM = "user_name";

	public User getCurrentUser() {
		final Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final String username = principal.getClaimAsString(JWT_USERNAME_CLAIM);
		return new User(username);
	}
}
