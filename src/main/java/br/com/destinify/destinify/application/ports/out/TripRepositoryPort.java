package br.com.destinify.destinify.application.ports.out;

import br.com.destinify.destinify.application.dto.request.TripFilterQuery;
import br.com.destinify.destinify.domain.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripRepositoryPort {

    Trip save(Trip trip);
    Optional<Trip> findById(UUID id);
    void deleteById(UUID id);
    Page<Trip> findWithFilters(TripFilterQuery filter, Pageable pageable);
}
