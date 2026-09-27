package com.example.week07_lab.flight.application;

import com.example.week07_lab.flight.domain.FlightService;
import com.example.week07_lab.flight.dto.FlightCreateRequest;
import com.example.week07_lab.flight.dto.FlightResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

	private final FlightService flightService;

	/** Mision 1: crear vuelo (endpoint sin proteccion). */
	@PostMapping("/create")
	public ResponseEntity<FlightResponse> create(@Valid @RequestBody FlightCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(flightService.create(request));
	}

	/**
	 * Mision 4: busqueda de vuelos (protegido con JWT).
	 * Ej: /flights/search?flightNumber=AA9&airline=fly&departureFrom=2027-01-01T00:00:00&departureTo=2027-12-31T23:59:59
	 */
	@GetMapping("/search")
	public ResponseEntity<List<FlightResponse>> search(
			@RequestParam(required = false) String flightNumber,
			@RequestParam(required = false) String airline,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureFrom,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTo) {
		return ResponseEntity.ok(flightService.search(flightNumber, airline, departureFrom, departureTo));
	}
}
