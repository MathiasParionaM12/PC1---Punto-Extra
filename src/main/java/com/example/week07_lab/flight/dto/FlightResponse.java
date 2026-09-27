package com.example.week07_lab.flight.dto;

import com.example.week07_lab.flight.domain.Flight;

import java.time.LocalDateTime;

/** DTO de salida: no exponemos la entidad directamente. */
public record FlightResponse(
		Long id,
		String flightNumber,
		String airline,
		LocalDateTime departureTime,
		LocalDateTime arrivalTime,
		Integer availableSeats
) {

	public static FlightResponse from(Flight flight) {
		return new FlightResponse(
				flight.getId(),
				flight.getFlightNumber(),
				flight.getAirline(),
				flight.getDepartureTime(),
				flight.getArrivalTime(),
				flight.getAvailableSeats()
		);
	}
}
