package br.com.destinify.destinify.application.ports.in.reservation;

import br.com.destinify.destinify.application.dto.request.CreateReservationCommand;
import br.com.destinify.destinify.domain.model.Reservation;

public interface CreateReservationUseCase {

    Reservation execute(CreateReservationCommand command);
}
