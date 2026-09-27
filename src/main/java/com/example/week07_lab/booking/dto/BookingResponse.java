package com.example.week07_lab.booking.dto;

import com.example.week07_lab.booking.domain.Booking;
import com.example.week07_lab.flight.domain.Flight;

import java.time.LocalDateTime;

public record BookingResponse(
		Long id,
		Long customerId,
		String customerFirstName,
		String customerLastName,
		LocalDateTime bookingDate,
		Long flightId,
		String flightNumber,
		String airline,
		LocalDateTime departureTime,
		LocalDateTime arrivalTime
) {

	public static BookingResponse from(Booking booking) {
		Flight flight = booking.getFlight();
		return new BookingResponse(
				booking.getId(),
				booking.getCustomer().getId(),
				booking.getCustomerFirstName(),
				booking.getCustomerLastName(),
				booking.getBookingDate(),
				flight.getId(),
				flight.getFlightNumber(),
				flight.getAirline(),
				flight.getDepartureTime(),
				flight.getArrivalTime()
		);
	}
}
