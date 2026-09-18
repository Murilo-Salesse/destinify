package br.com.destinify.destinify.application.service.passenger;

import br.com.destinify.destinify.application.ports.out.PassengerRepositoryPort;
import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.domain.enums.DocumentType;
import br.com.destinify.destinify.domain.exception.BusinessException;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import br.com.destinify.destinify.domain.model.Passenger;
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
class BoardPassengerServiceTest {

    @Mock
    private PassengerRepositoryPort passengerRepositoryPort;

    @Mock
    private ReservationRepositoryPort reservationRepositoryPort;

    @InjectMocks
    private BoardPassengerService boardPassengerService;

    @Test
    @DisplayName("Deve realizar embarque do passageiro quando reserva estiver confirmada")
    void shouldBoardPassengerSuccessfully() {
        String ticket = "DST-12345678";
        UUID resId = UUID.randomUUID();
        Passenger passenger = Passenger.createNew(
                resId, "Julia Mendes", "111", DocumentType.CPF, 28, "Birigui"
        );

        Reservation reservation = Reservation.createNew(
                UUID.randomUUID(), "Julia", "julia@email.com", "189999",
                BigDecimal.valueOf(100), 1, "Terminal", OffsetDateTime.now().plusMinutes(10)
        );
        reservation.confirm();

        when(passengerRepositoryPort.findByTicketCode(ticket)).thenReturn(Optional.of(passenger));
        when(reservationRepositoryPort.findById(resId)).thenReturn(Optional.of(reservation));
        when(passengerRepositoryPort.save(any(Passenger.class))).thenAnswer(inv -> inv.getArgument(0));

        Passenger result = boardPassengerService.execute(ticket);

        assertNotNull(result);
        assertTrue(result.isBoarded());
        verify(passengerRepositoryPort, times(1)).save(passenger);
    }

    @Test
    @DisplayName("Deve falhar embarque quando a reserva não estiver confirmada")
    void shouldThrowWhenReservationNotConfirmed() {
        String ticket = "DST-12345678";
        UUID resId = UUID.randomUUID();
        Passenger passenger = Passenger.createNew(
                resId, "Julia Mendes", "111", DocumentType.CPF, 28, "Birigui"
        );

        Reservation reservation = Reservation.createNew(
                UUID.randomUUID(), "Julia", "julia@email.com", "189999",
                BigDecimal.valueOf(100), 1, "Terminal", OffsetDateTime.now().plusMinutes(10)
        ); // PENDING

        when(passengerRepositoryPort.findByTicketCode(ticket)).thenReturn(Optional.of(passenger));
        when(reservationRepositoryPort.findById(resId)).thenReturn(Optional.of(reservation));

        assertThrows(BusinessException.class, () -> boardPassengerService.execute(ticket));
    }

    @Test
    @DisplayName("Deve falhar quando o ticketCode não existir")
    void shouldThrowWhenTicketNotFound() {
        when(passengerRepositoryPort.findByTicketCode("DST-UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> boardPassengerService.execute("DST-UNKNOWN"));
    }
}
