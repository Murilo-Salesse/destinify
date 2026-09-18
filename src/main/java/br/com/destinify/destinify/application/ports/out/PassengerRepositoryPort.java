package br.com.destinify.destinify.application.ports.out;

import br.com.destinify.destinify.domain.model.Passenger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PassengerRepositoryPort {

    Passenger save(Passenger passenger);

    Optional<Passenger> findByTicketCode(String ticketCode);

    List<Passenger> findByReservationId(UUID reservationId);
}
