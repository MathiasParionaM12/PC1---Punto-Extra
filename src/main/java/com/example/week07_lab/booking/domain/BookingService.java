package com.example.week07_lab.booking.domain;

import com.example.week07_lab.booking.dto.BookingRequest;
import com.example.week07_lab.booking.dto.BookingResponse;
import com.example.week07_lab.booking.infrastructure.BookingRepository;
import com.example.week07_lab.flight.domain.Flight;
import com.example.week07_lab.flight.infrastructure.FlightRepository;
import com.example.week07_lab.shared.exception.BadRequestException;
import com.example.week07_lab.shared.exception.ConflictException;
import com.example.week07_lab.shared.exception.ForbiddenException;
import com.example.week07_lab.shared.exception.NotFoundException;
import com.example.week07_lab.user.domain.User;
import com.example.week07_lab.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BookingService {

	private final BookingRepository bookingRepository;
	private final FlightRepository flightRepository;
	private final UserService userService;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public BookingResponse book(String customerEmail, BookingRequest request) {
		// Auto-calculado: el cliente es el usuario autenticado (no viene en el body)
		User customer = userService.getByEmail(customerEmail);

		// Bloqueamos el vuelo hasta el commit para que dos reservas concurrentes no sobrevendan
		Flight flight = flightRepository.findByIdForUpdate(request.getFlightId())
				.orElseThrow(() -> new NotFoundException("No existe el vuelo con id " + request.getFlightId()));

		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

		// No reservar vuelos pasados ni en transito (ya despegaron)
		if (!flight.getDepartureTime().isAfter(now)) {
			throw new BadRequestException(flight.getArrivalTime().isAfter(now)
					? "El vuelo " + flight.getFlightNumber() + " ya esta en transito"
					: "El vuelo " + flight.getFlightNumber() + " ya partio (vuelo pasado)");
		}

		// No sobrevender
		if (flight.getAvailableSeats() <= 0) {
			throw new ConflictException("El vuelo " + flight.getFlightNumber() + " no tiene asientos disponibles");
		}

		// Sin conflicto de horario con otras reservas del mismo usuario
		if (bookingRepository.existsScheduleConflict(customer.getId(), flight.getDepartureTime(), flight.getArrivalTime())) {
			throw new ConflictException("Ya tienes una reserva en un vuelo que se superpone con el horario del vuelo "
					+ flight.getFlightNumber());
		}

		flight.setAvailableSeats(flight.getAvailableSeats() - 1);

		Booking booking = new Booking();
		booking.setCustomer(customer);
		booking.setFlight(flight);
		booking.setCustomerFirstName(customer.getFirstName());
		booking.setCustomerLastName(customer.getLastName());
		booking.setBookingDate(now);
		booking = bookingRepository.save(booking);

		// Mision 6: el email se genera cuando la transaccion hace commit (reserva confirmada)
		eventPublisher.publishEvent(new BookingConfirmedEvent(
				booking.getId(),
				booking.getCustomerFirstName(),
				booking.getCustomerLastName(),
				customer.getEmail(),
				flight.getFlightNumber(),
				flight.getAirline(),
				flight.getDepartureTime(),
				flight.getArrivalTime(),
				booking.getBookingDate()
		));

		return BookingResponse.from(booking);
	}

	/** Un usuario solo puede ver sus propias reservas. */
	@Transactional(readOnly = true)
	public BookingResponse getById(String customerEmail, Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new NotFoundException("No existe la reserva con id " + bookingId));

		if (!booking.getCustomer().getEmail().equals(customerEmail)) {
			throw new ForbiddenException("La reserva " + bookingId + " pertenece a otro usuario");
		}

		return BookingResponse.from(booking);
	}
}
