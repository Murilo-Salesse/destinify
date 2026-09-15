package br.com.destinify.destinify.application.service.reservation;

import br.com.destinify.destinify.application.ports.in.reservation.GetReservationByIdUseCase;
import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import br.com.destinify.destinify.domain.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetReservationByIdService implements GetReservationByIdUseCase {

    private final ReservationRepositoryPort reservationRepositoryPort;

    public GetReservationByIdService(ReservationRepositoryPort reservationRepositoryPort) {
        this.reservationRepositoryPort = reservationRepositoryPort;
    }

    @Override
    public Reservation execute(UUID id) {
        return reservationRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com o ID: " + id));
    }
}
