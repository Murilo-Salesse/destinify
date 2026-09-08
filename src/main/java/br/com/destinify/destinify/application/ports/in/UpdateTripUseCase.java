package br.com.destinify.destinify.application.ports.in;

import br.com.destinify.destinify.application.dto.request.UpdateTripCommand;
import br.com.destinify.destinify.domain.model.Trip;


public interface UpdateTripUseCase {

    Trip execute(UpdateTripCommand command);
}
