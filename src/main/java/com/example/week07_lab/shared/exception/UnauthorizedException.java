package com.example.week07_lab.shared.exception;

/** Credenciales invalidas o usuario no autenticado (401). */
public class UnauthorizedException extends RuntimeException {

	public UnauthorizedException(String message) {
		super(message);
	}
}
