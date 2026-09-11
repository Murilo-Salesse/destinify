package br.com.destinify.destinify.application.ports.in.trip;

import br.com.destinify.destinify.application.dto.request.CreateTripCommand;
import br.com.destinify.destinify.domain.model.Trip;

public interface CreateTripUseCase {

    Trip execute(CreateTripCommand command);
}
