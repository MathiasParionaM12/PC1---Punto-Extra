package com.example.week07_lab.booking.infrastructure;

import com.example.week07_lab.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	/**
	 * Dos intervalos [salidaA, llegadaA] y [salidaB, llegadaB] se superponen si
	 * salidaA < llegadaB y llegadaA > salidaB. Tambien cubre reservar dos veces el mismo vuelo.
	 */
	@Query("""
			select count(b) > 0 from Booking b
			where b.customer.id = :customerId
			  and b.flight.departureTime < :arrivalTime
			  and b.flight.arrivalTime > :departureTime
			""")
	boolean existsScheduleConflict(@Param("customerId") Long customerId,
								   @Param("departureTime") LocalDateTime departureTime,
								   @Param("arrivalTime") LocalDateTime arrivalTime);
}
