package com.example.week07_lab.shared.exception;

/** El usuario esta autenticado pero no puede acceder al recurso (403). */
public class ForbiddenException extends RuntimeException {

	public ForbiddenException(String message) {
		super(message);
	}
}
