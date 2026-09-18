package br.com.destinify.destinify.application.ports.out;

import br.com.destinify.destinify.application.dto.event.ReservationCreatedEvent;

public interface ReservationEventPublisherPort {
    void publishReservationCreated(ReservationCreatedEvent event);
}
