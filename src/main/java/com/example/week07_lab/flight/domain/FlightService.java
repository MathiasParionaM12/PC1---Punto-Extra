package com.example.week07_lab.flight.domain;

import com.example.week07_lab.flight.dto.FlightCreateRequest;
import com.example.week07_lab.flight.dto.FlightResponse;
import com.example.week07_lab.flight.infrastructure.FlightRepository;
import com.example.week07_lab.flight.infrastructure.FlightSpecifications;
import com.example.week07_lab.shared.exception.BadRequestException;
import com.example.week07_lab.shared.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

	private final FlightRepository flightRepository;

	@Transactional
	public FlightResponse create(FlightCreateRequest request) {
		// Hora de salida estrictamente anterior a la hora de llegada
		if (!request.getDepartureTime().isBefore(request.getArrivalTime())) {
			throw new BadRequestException("La hora de salida debe ser anterior a la hora de llegada");
		}

		// Numeros de vuelo unicos
		if (flightRepository.existsByFlightNumber(request.getFlightNumber())) {
			throw new ConflictException("Ya existe un vuelo con el numero " + request.getFlightNumber());
		}

		Flight flight = new Flight();
		flight.setFlightNumber(request.getFlightNumber());
		flight.setAirline(request.getAirline());
		flight.setDepartureTime(request.getDepartureTime());
		flight.setArrivalTime(request.getArrivalTime());
		flight.setAvailableSeats(request.getAvailableSeats());

		return FlightResponse.from(flightRepository.save(flight));
	}

	/** Mision 4: todos los filtros son opcionales y se combinan con AND. */
	@Transactional(readOnly = true)
	public List<FlightResponse> search(String flightNumber, String airline,
									   LocalDateTime departureFrom, LocalDateTime departureTo) {
		if (departureFrom != null && departureTo != null && departureFrom.isAfter(departureTo)) {
			throw new BadRequestException("departureFrom debe ser anterior o igual a departureTo");
		}

		return flightRepository.findAll(
						FlightSpecifications.search(flightNumber, airline, departureFrom, departureTo),
						Sort.by("departureTime", "id"))
				.stream()
				.map(FlightResponse::from)
				.toList();
	}
}
