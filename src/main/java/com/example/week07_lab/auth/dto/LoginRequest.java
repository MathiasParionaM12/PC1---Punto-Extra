package com.example.week07_lab.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

	@NotBlank(message = "El email es requerido")
	private String email;

	@NotBlank(message = "La contrasena es requerida")
	private String password;
}
