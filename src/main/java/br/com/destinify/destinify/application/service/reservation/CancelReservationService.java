package br.com.destinify.destinify.application.service.reservation;

import br.com.destinify.destinify.application.ports.in.reservation.CancelReservationUseCase;
import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import br.com.destinify.destinify.domain.model.Reservation;
import br.com.destinify.destinify.domain.model.Trip;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CancelReservationService implements CancelReservationUseCase {

    private final ReservationRepositoryPort reservationRepositoryPort;
    private final TripRepositoryPort tripRepositoryPort;

    public CancelReservationService(ReservationRepositoryPort reservationRepositoryPort, TripRepositoryPort tripRepositoryPort) {
        this.reservationRepositoryPort = reservationRepositoryPort;
        this.tripRepositoryPort = tripRepositoryPort;
    }

    @Override
    @Transactional
    public void execute(UUID id) {
        Reservation reservation = reservationRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com o ID: " + id));

        reservation.cancel();
        reservationRepositoryPort.save(reservation);

        // Devolve os assentos de volta para a viagem
        Trip trip = tripRepositoryPort.findById(reservation.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Viagem não encontrada com o ID: " + reservation.getTripId()));

        trip.releaseSeats(reservation.getSeatsCount());
        tripRepositoryPort.save(trip);
    }
}
