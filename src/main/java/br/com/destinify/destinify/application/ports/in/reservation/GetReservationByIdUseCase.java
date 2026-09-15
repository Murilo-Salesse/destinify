package br.com.destinify.destinify.application.ports.in.reservation;

import br.com.destinify.destinify.domain.model.Reservation;

import java.util.UUID;

public interface GetReservationByIdUseCase {
    Reservation execute(UUID id);
}
