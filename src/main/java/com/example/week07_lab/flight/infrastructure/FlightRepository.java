package com.example.week07_lab.flight.infrastructure;

import com.example.week07_lab.flight.domain.Flight;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long>, JpaSpecificationExecutor<Flight> {

	boolean existsByFlightNumber(String flightNumber);

	/**
	 * Bloquea la fila del vuelo (SELECT ... FOR UPDATE) mientras se reserva:
	 * dos reservas simultaneas del ultimo asiento no pueden sobrevender el vuelo.
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select f from Flight f where f.id = :id")
	Optional<Flight> findByIdForUpdate(@Param("id") Long id);
}
