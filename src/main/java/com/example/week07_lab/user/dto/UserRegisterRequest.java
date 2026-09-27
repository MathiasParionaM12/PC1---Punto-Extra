package com.example.week07_lab.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {

	@NotBlank(message = "El nombre es requerido")
	@Pattern(regexp = ".*[A-Z].*", message = "El nombre debe tener al menos 1 letra mayuscula (A-Z)")
	private String firstName;

	@NotBlank(message = "El apellido es requerido")
	@Pattern(regexp = ".*[A-Z].*", message = "El apellido debe tener al menos 1 letra mayuscula (A-Z)")
	private String lastName;

	@NotBlank(message = "El email es requerido")
	@Email(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "El email no tiene un formato valido")
	private String email;

	@NotBlank(message = "La contrasena es requerida")
	@Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
	@Pattern(
			regexp = "^(?=.*[A-Za-z])(?=.*\\d).*$",
			message = "La contrasena debe tener al menos 1 letra y 1 numero"
	)
	private String password;
}
