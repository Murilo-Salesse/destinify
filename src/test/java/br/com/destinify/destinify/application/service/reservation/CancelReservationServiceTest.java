package br.com.destinify.destinify.application.service.reservation;

import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.enums.ReservationStatus;
import br.com.destinify.destinify.domain.enums.TripStatus;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelReservationServiceTest {

    @Mock
    private ReservationRepositoryPort reservationRepositoryPort;

    @Mock
    private TripRepositoryPort tripRepositoryPort;

    @InjectMocks
    private CancelReservationService cancelReservationService;

    @Test
    @DisplayName("Deve cancelar reserva e devolver assentos para a viagem")
    void shouldCancelReservationAndReleaseSeats() {
        UUID tripId = UUID.randomUUID();
        Trip trip = new Trip(
                tripId, "Viagem Bonito", "Bonito, MS",
                OffsetDateTime.now().plusDays(10), OffsetDateTime.now().plusDays(15),
                BigDecimal.valueOf(800.00), 20, 10, "Ecoturismo", "Transporte",
                "http://img.png", TripStatus.PUBLISHED,
                OffsetDateTime.now(), OffsetDateTime.now()
        );

        UUID resId = UUID.randomUUID();
        Reservation reservation = Reservation.createNew(
                tripId, "Roberto Silva", "roberto@email.com", "18999993333",
                BigDecimal.valueOf(1600.00), 2, "Terminal",
                OffsetDateTime.now().plusMinutes(15)
        );

        when(reservationRepositoryPort.findById(resId)).thenReturn(Optional.of(reservation));
        when(tripRepositoryPort.findById(tripId)).thenReturn(Optional.of(trip));

        cancelReservationService.execute(resId);

        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        assertEquals(12, trip.getAvailableSeats());

        verify(tripRepositoryPort, times(1)).save(trip);
        verify(reservationRepositoryPort, times(1)).save(reservation);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando reserva não existe")
    void shouldThrowExceptionWhenReservationNotFound() {
        UUID resId = UUID.randomUUID();
        when(reservationRepositoryPort.findById(resId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cancelReservationService.execute(resId));
    }
}
