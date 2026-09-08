package br.com.destinify.destinify.application.service;

import br.com.destinify.destinify.application.dto.request.UpdateTripCommand;
import br.com.destinify.destinify.application.ports.in.UpdateTripUseCase;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import br.com.destinify.destinify.domain.model.Trip;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UpdateTripService implements UpdateTripUseCase {

    private final TripRepositoryPort tripRepositoryPort;

    public UpdateTripService(TripRepositoryPort tripRepositoryPort) {
        this.tripRepositoryPort = tripRepositoryPort;
    }

    @Override
    @Transactional
    public Trip execute(UpdateTripCommand command) {

        Trip trip = tripRepositoryPort.findById(command.tripId())
                .orElseThrow(() -> new ResourceNotFoundException("Viagem não encontrada com o ID: " + command.tripId()));

        trip.updateTrip(
                command.title(),
                command.departureAt(),
                command.returnAt(),
                command.price(),
                command.totalSeats(),
                command.description(),
                command.includedItems(),
                command.coverImageUrl());

        return tripRepositoryPort.save(trip);
    }
}
