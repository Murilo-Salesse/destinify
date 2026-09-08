package br.com.destinify.destinify.application.ports.in;

import br.com.destinify.destinify.application.dto.request.TripFilterQuery;
import br.com.destinify.destinify.domain.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchTripUseCase {

    Page<Trip> execute(TripFilterQuery filter, Pageable pageable);
}
