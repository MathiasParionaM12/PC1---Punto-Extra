package com.example.week07_lab.shared.exception;

/** El request es valido pero choca con el estado actual de la BD (ej: numero de vuelo duplicado). */
public class ConflictException extends RuntimeException {

	public ConflictException(String message) {
		super(message);
	}
}
