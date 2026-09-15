package br.com.destinify.destinify.application.ports.out;

import br.com.destinify.destinify.domain.model.Reservation;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepositoryPort {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(UUID id);
}

