package br.com.destinify.destinify.application.ports.in;

import br.com.destinify.destinify.domain.model.Trip;

import java.util.UUID;

public interface GetTripByIdUseCase {
    Trip execute(UUID tripId);
}
