package com.example.week07_lab.shared.exception;

/** Recurso que no existe (404). */
public class NotFoundException extends RuntimeException {

	public NotFoundException(String message) {
		super(message);
	}
}
