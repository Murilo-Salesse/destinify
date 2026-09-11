package br.com.destinify.destinify.application.service.trip;

import br.com.destinify.destinify.application.dto.request.TripFilterQuery;
import br.com.destinify.destinify.application.ports.in.trip.SearchTripUseCase;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchTripService implements SearchTripUseCase {

    private final TripRepositoryPort tripRepositoryPort;

    public SearchTripService(TripRepositoryPort tripRepositoryPort) {
        this.tripRepositoryPort = tripRepositoryPort;
    }

    @Override
    public Page<Trip> execute(TripFilterQuery filter, Pageable pageable) {
        return tripRepositoryPort.findWithFilters(filter, pageable);
    }
}