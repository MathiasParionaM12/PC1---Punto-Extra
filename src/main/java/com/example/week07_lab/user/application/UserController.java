package com.example.week07_lab.user.application;

import com.example.week07_lab.user.domain.UserService;
import com.example.week07_lab.user.dto.UserRegisterRequest;
import com.example.week07_lab.user.dto.UserRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/** Mision 2: registro de usuarios (endpoint sin proteccion). */
	@PostMapping("/register")
	public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
	}
}
