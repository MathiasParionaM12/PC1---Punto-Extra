package com.example.week07_lab.booking.domain;

import java.time.LocalDateTime;

/** Se publica al confirmar una reserva; lo consume el generador del email (mision 6). */
public record BookingConfirmedEvent(
		Long bookingId,
		String customerFirstName,
		String customerLastName,
		String customerEmail,
		String flightNumber,
		String airline,
		LocalDateTime departureTime,
		LocalDateTime arrivalTime,
		LocalDateTime bookingDate
) {
}
