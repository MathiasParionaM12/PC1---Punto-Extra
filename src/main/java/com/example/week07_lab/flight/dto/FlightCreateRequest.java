package com.example.week07_lab.flight.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FlightCreateRequest {

	@NotBlank(message = "El numero de vuelo es requerido")
	@Pattern(
			regexp = "^[A-Z0-9]{1,6}$",
			message = "El numero de vuelo solo acepta A-Z y 0-9, maximo 6 caracteres (ej: AA984)"
	)
	private String flightNumber;

	@NotBlank(message = "La aerolinea es requerida")
	private String airline;

	@NotNull(message = "La hora de salida es requerida")
	private LocalDateTime departureTime;

	@NotNull(message = "La hora de llegada es requerida")
	private LocalDateTime arrivalTime;

	@NotNull(message = "Los asientos disponibles son requeridos")
	@Min(value = 1, message = "Los asientos disponibles deben ser mayores a 0")
	private Integer availableSeats;
}
