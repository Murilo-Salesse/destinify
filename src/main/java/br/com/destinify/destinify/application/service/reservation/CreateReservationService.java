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
    private final br.com.destinify.destinify.application.ports.out.ReservationEventPublisherPort reservationEventPublisherPort;

    public CreateReservationService(ReservationRepositoryPort reservationRepositoryPort,
                                  TripRepositoryPort tripRepositoryPort,
                                  br.com.destinify.destinify.application.ports.out.ReservationEventPublisherPort reservationEventPublisherPort) {
        this.reservationRepositoryPort = reservationRepositoryPort;
        this.tripRepositoryPort = tripRepositoryPort;
        this.reservationEventPublisherPort = reservationEventPublisherPort;
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

        if (command.passengers() != null) {
            for (var pCmd : command.passengers()) {
                reservation.addPassenger(br.com.destinify.destinify.domain.model.Passenger.createNew(
                        reservation.getId(),
                        pCmd.fullName(),
                        pCmd.documentNumber(),
                        pCmd.documentType(),
                        pCmd.age(),
                        pCmd.city()
                ));
            }
        }

        Reservation savedReservation = reservationRepositoryPort.save(reservation);

        // Publica evento para o RabbitMQ com TTL de expiração
        reservationEventPublisherPort.publishReservationCreated(
                new br.com.destinify.destinify.application.dto.event.ReservationCreatedEvent(
                        savedReservation.getId(),
                        savedReservation.getTripId(),
                        savedReservation.getSeatsCount()
                )
        );

        return savedReservation;
    }
}
