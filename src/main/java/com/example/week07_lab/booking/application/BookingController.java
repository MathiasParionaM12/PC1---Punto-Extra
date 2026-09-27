package com.example.week07_lab.booking.application;

import com.example.week07_lab.booking.domain.BookingService;
import com.example.week07_lab.booking.dto.BookingRequest;
import com.example.week07_lab.booking.dto.BookingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mision 5 (protegido con JWT). Ojo con las rutas del enunciado:
 * se reserva en /flights/book (plural) y se consulta en /flight/book/{id} (singular).
 */
@RestController
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	/** authentication.getName() = email del usuario del JWT (lo coloca JwtAuthenticationFilter). */
	@PostMapping("/flights/book")
	public ResponseEntity<BookingResponse> book(@Valid @RequestBody BookingRequest request, Authentication authentication) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.book(authentication.getName(), request));
	}

	@GetMapping("/flight/book/{id}")
	public ResponseEntity<BookingResponse> getById(@PathVariable Long id, Authentication authentication) {
		return ResponseEntity.ok(bookingService.getById(authentication.getName(), id));
	}
}
