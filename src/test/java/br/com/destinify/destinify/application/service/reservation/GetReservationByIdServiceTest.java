package br.com.destinify.destinify.application.service.reservation;

import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
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
class GetReservationByIdServiceTest {

    @Mock
    private ReservationRepositoryPort reservationRepositoryPort;

    @InjectMocks
    private GetReservationByIdService getReservationByIdService;

    @Test
    @DisplayName("Deve buscar reserva por ID")
    void shouldFindReservationById() {
        UUID resId = UUID.randomUUID();
        Reservation reservation = Reservation.createNew(
                UUID.randomUUID(), "Marcos Paulo", "marcos@email.com", "18999990000",
                BigDecimal.valueOf(100), 1, "Terminal",
                OffsetDateTime.now().plusMinutes(15)
        );

        when(reservationRepositoryPort.findById(resId)).thenReturn(Optional.of(reservation));

        Reservation result = getReservationByIdService.execute(resId);

        assertNotNull(result);
        assertEquals(reservation.getId(), result.getId());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando reserva não existe")
    void shouldThrowExceptionWhenNotFound() {
        UUID resId = UUID.randomUUID();
        when(reservationRepositoryPort.findById(resId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> getReservationByIdService.execute(resId));
    }
}
