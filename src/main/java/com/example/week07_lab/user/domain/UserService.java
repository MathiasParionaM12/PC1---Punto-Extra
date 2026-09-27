package com.example.week07_lab.user.domain;

import com.example.week07_lab.shared.exception.ConflictException;
import com.example.week07_lab.shared.exception.UnauthorizedException;
import com.example.week07_lab.user.dto.UserRegisterRequest;
import com.example.week07_lab.user.dto.UserRegisterResponse;
import com.example.week07_lab.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserRegisterResponse register(UserRegisterRequest request) {
		String email = request.getEmail().trim().toLowerCase();
		if (userRepository.existsByEmail(email)) {
			throw new ConflictException("Ya existe un usuario con el email " + email);
		}

		User user = new User();
		user.setFirstName(request.getFirstName().trim());
		user.setLastName(request.getLastName().trim());
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		return new UserRegisterResponse(userRepository.save(user).getId());
	}

	/** Usuario dueno del JWT de la request actual (el filtro JWT guarda su email como principal). */
	@Transactional(readOnly = true)
	public User getByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UnauthorizedException("El usuario del token ya no existe"));
	}
}
