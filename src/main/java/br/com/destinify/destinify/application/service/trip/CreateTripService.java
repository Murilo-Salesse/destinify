package br.com.destinify.destinify.application.service.trip;

import br.com.destinify.destinify.application.dto.request.CreateTripCommand;
import br.com.destinify.destinify.application.ports.in.trip.CreateTripUseCase;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.model.Trip;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTripService implements CreateTripUseCase {

    private final TripRepositoryPort tripRepositoryPort;

    public CreateTripService(TripRepositoryPort tripRepositoryPort) {
        this.tripRepositoryPort = tripRepositoryPort;
    }

    @Override
    @Transactional
    public Trip execute(CreateTripCommand command) {
        Trip newTrip = Trip.createNew(
                command.title(),
                command.destination(),
                command.departureAt(),
                command.returnAt(),
                command.price(),
                command.totalSeats(),
                command.description(),
                command.includedItems(),
                command.coverImageUrl()

        );

        return tripRepositoryPort.save(newTrip);
    }
}