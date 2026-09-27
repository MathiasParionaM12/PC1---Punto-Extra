package com.example.week07_lab.shared.exception;

/** Reglas de negocio que no se pueden expresar con anotaciones de validacion. */
public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}
}
