package br.com.destinify.destinify.application.dto.request;

import java.util.List;
import java.util.UUID;

public record CreateReservationCommand(
        UUID tripId,
        String contactName,
        String contactEmail,
        String contactPhone,
        Integer seatsCount,
        String boardingLocation,
        List<CreatePassengerCommand> passengers
) {}