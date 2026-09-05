package br.com.destinify.destinify.application.service;

import br.com.destinify.destinify.application.ports.in.GetTripByIdUseCase;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.exception.BusinessException;
import br.com.destinify.destinify.domain.model.Trip;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindTripByIdService implements GetTripByIdUseCase {

    private final TripRepositoryPort tripRepositoryPort;

    public FindTripByIdService(TripRepositoryPort tripRepositoryPort) {
        this.tripRepositoryPort = tripRepositoryPort;
    }

    @Override
    public Trip execute(UUID tripId) {

        return tripRepositoryPort.findById(tripId)
                .orElseThrow(() -> new BusinessException("Trip not found with id: " + tripId));
    }
}
