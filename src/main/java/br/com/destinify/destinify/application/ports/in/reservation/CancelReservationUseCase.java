package br.com.destinify.destinify.application.ports.in.reservation;

import java.util.UUID;

public interface CancelReservationUseCase {
    void execute(UUID id);
}
