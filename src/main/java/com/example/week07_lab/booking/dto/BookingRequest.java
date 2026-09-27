package com.example.week07_lab.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Mision 5: el unico input es el vuelo; los datos del cliente salen del JWT. */
@Getter
@Setter
public class BookingRequest {

	@NotNull(message = "El flightId es requerido")
	private Long flightId;
}
