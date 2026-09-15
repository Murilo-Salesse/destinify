package br.com.destinify.destinify.application.service.reservation;

import br.com.destinify.destinify.application.dto.request.CreateReservationCommand;
import br.com.destinify.destinify.application.ports.in.reservation.CreateReservationUseCase;
import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import br.com.destinify.destinify.domain.model.Reservation;
import br.com.destinify.destinify.domain.model.Trip;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class CreateReservationService implements CreateReservationUseCase {

    private final ReservationRepositoryPort reservationRepositoryPort;
    private final TripRepositoryPort tripRepositoryPort;

    public CreateReservationService(ReservationRepositoryPort reservationRepositoryPort, TripRepositoryPort tripRepositoryPort) {
        this.reservationRepositoryPort = reservationRepositoryPort;
        this.tripRepositoryPort = tripRepositoryPort;
    }

    @Override
    @Transactional
    public Reservation execute(CreateReservationCommand command) {

        Trip trip = tripRepositoryPort.findById(command.tripId())
                .orElseThrow(() -> new ResourceNotFoundException("Viagem não encontrada: " + command.tripId()));

        BigDecimal totalAmount = trip.getPrice().multiply(BigDecimal.valueOf(command.seatsCount()));
        OffsetDateTime expiresAt = OffsetDateTime.now().plusMinutes(15);

        trip.reserveSeats(command.seatsCount());
        tripRepositoryPort.save(trip);

        Reservation reservation = Reservation.createNew(
                command.tripId(),
                command.contactName(),
                command.contactEmail(),
                command.contactPhone(),
                totalAmount,
                command.seatsCount(),
                command.boardingLocation(),
                expiresAt
        );
        return reservationRepositoryPort.save(reservation);
    }
}
