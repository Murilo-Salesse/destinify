package br.com.destinify.destinify.domain.model;

import br.com.destinify.destinify.domain.enums.DocumentType;
import br.com.destinify.destinify.domain.enums.ReservationStatus;
import br.com.destinify.destinify.domain.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    private Reservation createPendingReservation() {
        return Reservation.createNew(
                UUID.randomUUID(),
                "Lucas Alcantara",
                "lucas@email.com",
                "18999991111",
                BigDecimal.valueOf(350.00),
                2,
                "Rodoviária de Birigui",
                OffsetDateTime.now().plusMinutes(15)
        );
    }

    @Test
    @DisplayName("Deve criar nova reserva pendente via factory")
    void shouldCreateNewReservation() {
        Reservation res = createPendingReservation();

        assertNotNull(res.getId());
        assertEquals(ReservationStatus.PENDING, res.getStatus());
        assertEquals(2, res.getSeatsCount());
        assertNotNull(res.getExpiresAt());
    }

    @Test
    @DisplayName("Deve adicionar passageiros respeitando a capacidade reservada")
    void shouldAddPassengersRespectingCapacity() {
        Reservation res = createPendingReservation();
        Passenger p1 = Passenger.createNew(res.getId(), "João", "111.111.111-11", DocumentType.CPF, 25, "Birigui");
        Passenger p2 = Passenger.createNew(res.getId(), "Maria", "222.222.222-22", DocumentType.CPF, 26, "Birigui");
        Passenger p3 = Passenger.createNew(res.getId(), "José", "333.333.333-33", DocumentType.CPF, 27, "Birigui");

        res.addPassenger(p1);
        res.addPassenger(p2);
        assertEquals(2, res.getPassengers().size());

        BusinessException ex = assertThrows(BusinessException.class, () -> res.addPassenger(p3));
        assertTrue(ex.getMessage().contains("A quantidade de passageiros não pode exceder"));
    }

    @Test
    @DisplayName("Deve confirmar reserva pendente dentro do prazo")
    void shouldConfirmPendingReservation() {
        Reservation res = createPendingReservation();

        res.confirm();

        assertEquals(ReservationStatus.CONFIRMED, res.getStatus());
        assertNotNull(res.getConfirmedAt());
    }

    @Test
    @DisplayName("Deve expirar reserva ao confirmar se já passou da data de expiração")
    void shouldExpireWhenConfirmingPastDeadline() {
        Reservation res = Reservation.createNew(
                UUID.randomUUID(), "Lucas", "lucas@email.com", "18999991111",
                BigDecimal.valueOf(300), 1, "Terminal",
                OffsetDateTime.now().minusMinutes(5)
        );

        assertThrows(BusinessException.class, res::confirm);
        assertEquals(ReservationStatus.EXPIRED, res.getStatus());
    }

    @Test
    @DisplayName("Deve cancelar reserva pendente ou confirmada")
    void shouldCancelReservation() {
        Reservation res = createPendingReservation();
        res.cancel();
        assertEquals(ReservationStatus.CANCELLED, res.getStatus());

        Reservation confirmed = createPendingReservation();
        confirmed.confirm();
        confirmed.cancel();
        assertEquals(ReservationStatus.CANCELLED, confirmed.getStatus());
    }

    @Test
    @DisplayName("Deve falhar ao cancelar reserva já cancelada ou expirada")
    void shouldThrowWhenCancellingInvalidStatus() {
        Reservation res = createPendingReservation();
        res.cancel();
        assertThrows(BusinessException.class, res::cancel);

        Reservation expired = createPendingReservation();
        expired.expire();
        assertThrows(BusinessException.class, expired::cancel);
    }

    @Test
    @DisplayName("Deve expirar reserva pendente")
    void shouldExpirePendingReservation() {
        Reservation res = createPendingReservation();
        res.expire();
        assertEquals(ReservationStatus.EXPIRED, res.getStatus());
    }
}
