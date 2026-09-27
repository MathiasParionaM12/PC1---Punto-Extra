package com.example.week07_lab.booking.infrastructure;

import com.example.week07_lab.booking.domain.BookingConfirmedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mision 6: genera flight_booking_email_${booking_id}.txt cuando la reserva queda confirmada.
 * AFTER_COMMIT: si la reserva falla (rollback) no se genera ningun email.
 */
@Slf4j
@Component
public class BookingEmailWriter {

	/** ISO 8601 siempre con segundos, ej: 2027-06-01T10:00:00 */
	private static final DateTimeFormatter ISO_8601 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	private final Path outputDir;

	public BookingEmailWriter(@Value("${booking.email.dir}") String outputDir) {
		this.outputDir = Path.of(outputDir).toAbsolutePath().normalize();
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onBookingConfirmed(BookingConfirmedEvent event) {
		Path file = outputDir.resolve("flight_booking_email_" + event.bookingId() + ".txt");
		try {
			Files.createDirectories(outputDir);
			Files.writeString(file, render(event), StandardCharsets.UTF_8);
			log.info("Email de confirmacion generado: {}", file);
		} catch (IOException e) {
			// La reserva ya esta confirmada en la BD: no la revertimos por un fallo al escribir el email
			log.error("No se pudo generar el email de la reserva {} en {}", event.bookingId(), file, e);
		}
	}

	private String render(BookingConfirmedEvent event) {
		return """
				To: %s
				Subject: Fly Away Travel - Confirmacion de reserva #%d

				Hola %s %s,

				Tu reserva ha sido confirmada. Estos son los detalles:

				Reserva:           #%d
				Pasajero:          %s %s
				Numero de vuelo:   %s
				Aerolinea:         %s
				Salida:            %s
				Llegada:           %s
				Fecha de reserva:  %s

				Gracias por volar con Fly Away Travel!
				""".formatted(
				event.customerEmail(), event.bookingId(),
				event.customerFirstName(), event.customerLastName(),
				event.bookingId(),
				event.customerFirstName(), event.customerLastName(),
				event.flightNumber(),
				event.airline(),
				iso(event.departureTime()),
				iso(event.arrivalTime()),
				iso(event.bookingDate())
		);
	}

	private static String iso(LocalDateTime dateTime) {
		return dateTime.format(ISO_8601);
	}
}
