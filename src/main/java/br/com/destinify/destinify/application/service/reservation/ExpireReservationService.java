package br.com.destinify.destinify.application.service.reservation;

import br.com.destinify.destinify.application.ports.in.reservation.ExpireReservationUseCase;
import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.enums.ReservationStatus;
import br.com.destinify.destinify.domain.model.Reservation;
import br.com.destinify.destinify.domain.model.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ExpireReservationService implements ExpireReservationUseCase {

    private static final Logger log = LoggerFactory.getLogger(ExpireReservationService.class);

    private final ReservationRepositoryPort reservationRepositoryPort;
    private final TripRepositoryPort tripRepositoryPort;

    public ExpireReservationService(ReservationRepositoryPort reservationRepositoryPort,
                                  TripRepositoryPort tripRepositoryPort) {
        this.reservationRepositoryPort = reservationRepositoryPort;
        this.tripRepositoryPort = tripRepositoryPort;
    }

    @Override
    @Transactional
    public void execute(UUID reservationId) {
        Optional<Reservation> optionalReservation = reservationRepositoryPort.findById(reservationId);

        if (optionalReservation.isEmpty()) {
            log.warn("Reserva {} não encontrada para expiração.", reservationId);
            return;
        }

        Reservation reservation = optionalReservation.get();

        // Só expira se ainda estiver pendente (se já foi CONFIRMED ou CANCELLED, não faz nada)
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            log.info("Reserva {} já se encontra com status {}. Nenhuma ação de expiração necessária.",
                    reservationId, reservation.getStatus());
            return;
        }

        log.info("Expirando reserva {} e devolvendo {} assentos para a viagem {}",
                reservation.getId(), reservation.getSeatsCount(), reservation.getTripId());

        reservation.expire();
        reservationRepositoryPort.save(reservation);

        // Devolve os assentos para a viagem
        tripRepositoryPort.findById(reservation.getTripId()).ifPresent(trip -> {
            trip.releaseSeats(reservation.getSeatsCount());
            tripRepositoryPort.save(trip);
            log.info("Vagas devolvidas com sucesso. Novas vagas disponíveis: {}", trip.getAvailableSeats());
        });
    }
}
