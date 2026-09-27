package com.example.week07_lab.auth.domain;

import com.example.week07_lab.auth.dto.LoginRequest;
import com.example.week07_lab.auth.dto.TokenResponse;
import com.example.week07_lab.auth.infrastructure.JwtService;
import com.example.week07_lab.shared.exception.NotFoundException;
import com.example.week07_lab.shared.exception.UnauthorizedException;
import com.example.week07_lab.user.domain.User;
import com.example.week07_lab.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	@Transactional(readOnly = true)
	public TokenResponse login(LoginRequest request) {
		// Caso: email desconocido
		User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
				.orElseThrow(() -> new NotFoundException("No existe un usuario con ese email"));

		// Caso: contrasena incorrecta
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new UnauthorizedException("Contrasena incorrecta");
		}

		return new TokenResponse(jwtService.generateToken(user));
	}
}
