package br.com.destinify.destinify.application.service;

import br.com.destinify.destinify.application.dto.request.CreateTripCommand;
import br.com.destinify.destinify.application.ports.in.CreateTripUseCase;
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
        // 1. Cria a entidade de domínio executando as regras de validação do Trip
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

        // 2. Persiste através da porta de saída
        return tripRepositoryPort.save(newTrip);
    }
}