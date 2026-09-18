package br.com.destinify.destinify.application.service.reservation;

import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.domain.enums.ReservationStatus;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import br.com.destinify.destinify.domain.model.Reservation;
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
class ConfirmReservationServiceTest {

    @Mock
    private ReservationRepositoryPort reservationRepositoryPort;

    @InjectMocks
    private ConfirmReservationService confirmReservationService;

    @Test
    @DisplayName("Deve confirmar reserva com sucesso")
    void shouldConfirmReservationSuccessfully() {
        UUID reservationId = UUID.randomUUID();
        Reservation reservation = Reservation.createNew(
                UUID.randomUUID(), "Ana Paula", "ana@email.com", "18999992222",
                BigDecimal.valueOf(300.00), 1, "Terminal",
                OffsetDateTime.now().plusMinutes(15)
        );

        when(reservationRepositoryPort.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepositoryPort.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = confirmReservationService.execute(reservationId);

        assertNotNull(result);
        assertEquals(ReservationStatus.CONFIRMED, result.getStatus());
        verify(reservationRepositoryPort, times(1)).save(reservation);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando a reserva não existir")
    void shouldThrowExceptionWhenReservationNotFound() {
        UUID reservationId = UUID.randomUUID();
        when(reservationRepositoryPort.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> confirmReservationService.execute(reservationId));
        verify(reservationRepositoryPort, never()).save(any());
    }
}
