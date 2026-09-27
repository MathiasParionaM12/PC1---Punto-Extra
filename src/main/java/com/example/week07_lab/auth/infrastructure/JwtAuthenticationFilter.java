package com.example.week07_lab.auth.infrastructure;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Lee "Authorization: Bearer <jwt>" y, si el token es valido, autentica la request
 * con el email del usuario como principal. Si no hay token (o es invalido) la request
 * sigue sin autenticar y Spring Security responde 401 en los endpoints protegidos.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";

	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (StringUtils.hasText(header) && StringUtils.startsWithIgnoreCase(header, BEARER_PREFIX)) {
			jwtService.extractValidSubject(header.substring(BEARER_PREFIX.length()).trim())
					.ifPresent(email -> authenticate(request, email));
		}
		filterChain.doFilter(request, response);
	}

	private void authenticate(HttpServletRequest request, String email) {
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(email, null, List.of());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}
}
