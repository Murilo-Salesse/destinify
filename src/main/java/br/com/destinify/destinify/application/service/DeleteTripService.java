package br.com.destinify.destinify.application.service;

import br.com.destinify.destinify.application.ports.in.DeleteTripUseCase;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteTripService implements DeleteTripUseCase {

    private final TripRepositoryPort tripRepositoryPort;

    public DeleteTripService(TripRepositoryPort tripRepositoryPort) {
        this.tripRepositoryPort = tripRepositoryPort;
    }

    @Override
    public void execute(UUID id) {

        this.tripRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));

        this.tripRepositoryPort.deleteById(id);
    }
}
