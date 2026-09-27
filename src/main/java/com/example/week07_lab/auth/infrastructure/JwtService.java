package com.example.week07_lab.auth.infrastructure;

import com.example.week07_lab.user.domain.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtService {

	private final SecretKey signingKey;
	private final long expirationMillis;

	public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationMillis) {
		this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMillis = expirationMillis;
	}

	/** subject = email del usuario; userId va como claim extra. */
	public String generateToken(User user) {
		Date now = new Date();
		return Jwts.builder()
				.subject(user.getEmail())
				.claim("userId", user.getId())
				.issuedAt(now)
				.expiration(new Date(now.getTime() + expirationMillis))
				.signWith(signingKey)
				.compact();
	}

	/** Devuelve el email si el token tiene firma valida y no ha expirado. */
	public Optional<String> extractValidSubject(String token) {
		try {
			return Optional.ofNullable(Jwts.parser()
					.verifyWith(signingKey)
					.build()
					.parseSignedClaims(token)
					.getPayload()
					.getSubject());
		} catch (JwtException | IllegalArgumentException e) {
			return Optional.empty();
		}
	}
}
