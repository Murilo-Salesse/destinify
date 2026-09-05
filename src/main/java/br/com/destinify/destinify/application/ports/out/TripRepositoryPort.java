package br.com.destinify.destinify.application.ports.out;

import br.com.destinify.destinify.domain.model.Trip;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripRepositoryPort {

    Trip save(Trip trip);
    Optional<Trip> findById(UUID id);
    List<Trip> findAll();
}
