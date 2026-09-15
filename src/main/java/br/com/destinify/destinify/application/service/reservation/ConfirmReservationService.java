package br.com.destinify.destinify.application.service.reservation;

import br.com.destinify.destinify.application.ports.in.reservation.ConfirmReservationUseCase;
import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import br.com.destinify.destinify.domain.model.Reservation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ConfirmReservationService implements ConfirmReservationUseCase {

    private final ReservationRepositoryPort reservationRepositoryPort;

    public ConfirmReservationService(ReservationRepositoryPort reservationRepositoryPort) {
        this.reservationRepositoryPort = reservationRepositoryPort;
    }

    @Override
    @Transactional
    public Reservation execute(UUID id) {
        Reservation reservation = reservationRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com o ID: " + id));

        reservation.confirm();
        return reservationRepositoryPort.save(reservation);
    }
}
