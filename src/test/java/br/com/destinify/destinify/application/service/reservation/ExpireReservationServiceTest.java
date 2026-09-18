package br.com.destinify.destinify.application.service.reservation;

import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.enums.ReservationStatus;
import br.com.destinify.destinify.domain.enums.TripStatus;
import br.com.destinify.destinify.domain.model.Reservation;
import br.com.destinify.destinify.domain.model.Trip;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpireReservationServiceTest {

    @Mock
    private ReservationRepositoryPort reservationRepositoryPort;

    @Mock
    private TripRepositoryPort tripRepositoryPort;

    @InjectMocks
    private ExpireReservationService expireReservationService;

    @Test
    @DisplayName("Deve expirar reserva pendente e devolver assentos")
    void shouldExpirePendingReservationAndReleaseSeats() {
        UUID tripId = UUID.randomUUID();
        Trip trip = new Trip(
                tripId, "Viagem Campos", "Campos, SP",
                OffsetDateTime.now().plusDays(20), OffsetDateTime.now().plusDays(22),
                BigDecimal.valueOf(500.00), 20, 10, "Frio e fondue", "Transporte",
                "http://img.png", TripStatus.PUBLISHED,
                OffsetDateTime.now(), OffsetDateTime.now()
        );

        UUID resId = UUID.randomUUID();
        Reservation reservation = Reservation.createNew(
                tripId, "Fernanda Lima", "fernanda@email.com", "18999994444",
                BigDecimal.valueOf(1500.00), 3, "Terminal",
                OffsetDateTime.now().minusMinutes(1)
        );

        when(reservationRepositoryPort.findById(resId)).thenReturn(Optional.of(reservation));
        when(tripRepositoryPort.findById(tripId)).thenReturn(Optional.of(trip));

        expireReservationService.execute(resId);

        verify(tripRepositoryPort, times(1)).save(trip);
        verify(reservationRepositoryPort, times(1)).save(reservation);
    }

    @Test
    @DisplayName("Não deve alterar assentos se a reserva já estiver CONFIRMED ao receber expiração")
    void shouldNotReleaseSeatsIfReservationAlreadyConfirmed() {
        UUID tripId = UUID.randomUUID();
        UUID resId = UUID.randomUUID();
        Reservation reservation = Reservation.createNew(
                tripId, "Fernanda Lima", "fernanda@email.com", "18999994444",
                BigDecimal.valueOf(1500.00), 3, "Terminal",
                OffsetDateTime.now().plusMinutes(10)
        );
        reservation.confirm();

        when(reservationRepositoryPort.findById(resId)).thenReturn(Optional.of(reservation));

        expireReservationService.execute(resId);

        verify(tripRepositoryPort, never()).save(any());
    }
}
