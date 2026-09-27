package com.example.week07_lab.flight.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** unique = true: la BD tambien garantiza numeros de vuelo unicos (mision 1). */
	@Column(name = "flight_number", nullable = false, unique = true, length = 6)
	private String flightNumber;

	@Column(nullable = false)
	private String airline;

	@Column(name = "departure_time", nullable = false)
	private LocalDateTime departureTime;

	@Column(name = "arrival_time", nullable = false)
	private LocalDateTime arrivalTime;

	@Column(name = "available_seats", nullable = false)
	private Integer availableSeats;
}
