package br.com.destinify.destinify.application.ports.in;

import br.com.destinify.destinify.domain.model.Trip;

import java.util.List;

public interface GetAllTripsUseCase {
    List<Trip> execute();
}
