package br.com.destinify.destinify.application.ports.in.passenger;

import br.com.destinify.destinify.domain.model.Passenger;

public interface BoardPassengerUseCase {
    Passenger execute(String ticketCode);
}
