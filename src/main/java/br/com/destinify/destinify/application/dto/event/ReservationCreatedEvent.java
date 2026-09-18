package br.com.destinify.destinify.application.dto.event;

import java.util.UUID;

public record ReservationCreatedEvent(
        UUID reservationId,
        UUID tripId,
        int seatsCount
) {}
