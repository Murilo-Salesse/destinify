package br.com.destinify.destinify.application.service;

import br.com.destinify.destinify.application.ports.in.GetAllTripsUseCase;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.model.Trip;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTripsService implements GetAllTripsUseCase {

    private final TripRepositoryPort tripRepositoryPort;

    public GetAllTripsService(TripRepositoryPort tripRepositoryPort) {
        this.tripRepositoryPort = tripRepositoryPort;
    }

    @Override
    public List<Trip> execute() {
        return tripRepositoryPort.findAll();
    }
}
