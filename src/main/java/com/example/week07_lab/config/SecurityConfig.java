package com.example.week07_lab.config;

import com.example.week07_lab.auth.infrastructure.JwtAuthenticationFilter;
import com.example.week07_lab.user.infrastructure.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// Misiones 1, 2 y 3: endpoints sin proteccion
						.requestMatchers(HttpMethod.POST, "/flights/create", "/users/register", "/auth/login").permitAll()
						// Para que los errores (400, 404, ...) de los endpoints publicos no se conviertan en 401
						.requestMatchers("/error").permitAll()
						// Misiones 4 y 5 (y cualquier otro endpoint): requieren JWT
						.anyRequest().authenticated()
				)
				.exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedEntryPoint()))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.httpBasic(basic -> basic.disable())
				.formLogin(form -> form.disable())
				.build();
	}

	/** Sin token (o con token invalido/expirado) respondemos 401 con el mismo formato JSON del GlobalExceptionHandler. */
	private AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("""
					{"timestamp":"%s","status":401,"error":"Unauthorized","message":"Se requiere un token JWT valido (Authorization: Bearer <jwt>)"}"""
					.formatted(LocalDateTime.now()));
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Usuarios de Spring Security respaldados por nuestra tabla users.
	 * Declararlo evita ademas que Spring Boot cree un usuario en memoria con password generado.
	 */
	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return email -> userRepository.findByEmail(email)
				.map(user -> org.springframework.security.core.userdetails.User
						.withUsername(user.getEmail())
						.password(user.getPassword())
						.build())
				.orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con el email " + email));
	}
}
