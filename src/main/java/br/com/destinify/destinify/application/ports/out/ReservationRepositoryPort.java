package br.com.destinify.destinify.application.ports.out;

import br.com.destinify.destinify.domain.model.Reservation;

public interface ReservationRepositoryPort {

    Reservation save(Reservation reservation);
}
