package com.example.week07_lab.booking.domain;

import com.example.week07_lab.flight.domain.Flight;
import com.example.week07_lab.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	private User customer;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "flight_id", nullable = false)
	private Flight flight;

	/** Nombres del pasajero al momento de reservar (auto-calculados desde el usuario autenticado). */
	@Column(name = "customer_first_name", nullable = false)
	private String customerFirstName;

	@Column(name = "customer_last_name", nullable = false)
	private String customerLastName;

	@Column(name = "booking_date", nullable = false)
	private LocalDateTime bookingDate;
}
