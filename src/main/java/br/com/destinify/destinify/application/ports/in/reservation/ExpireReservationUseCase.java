package br.com.destinify.destinify.application.ports.in.reservation;

import java.util.UUID;

public interface ExpireReservationUseCase {
    void execute(UUID reservationId);
}
