package br.com.destinify.destinify.domain.model;

import br.com.destinify.destinify.domain.enums.TripStatus;
import br.com.destinify.destinify.domain.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TripTest {

    private Trip createValidTrip(int seats, TripStatus status) {
        return new Trip(
                UUID.randomUUID(),
                "Viagem Ilhabela",
                "Ilhabela, SP",
                OffsetDateTime.now().plusDays(5),
                OffsetDateTime.now().plusDays(7),
                BigDecimal.valueOf(350.00),
                seats,
                seats,
                "Praias e cachoeiras",
                "Transporte e seguro",
                "http://img.png",
                status,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }

    @Test
    @DisplayName("Deve criar nova viagem via factory com validação")
    void shouldCreateNewTripViaFactory() {
        Trip trip = Trip.createNew(
                "Ubatuba", "Ubatuba, SP",
                OffsetDateTime.now().plusDays(1), OffsetDateTime.now().plusDays(2),
                BigDecimal.valueOf(200), 20, "Desc", "Itens", "img"
        );

        assertNotNull(trip.getId());
        assertEquals(TripStatus.DRAFT, trip.getStatus());
        assertEquals(20, trip.getTotalSeats());
        assertEquals(20, trip.getAvailableSeats());
    }

    @Test
    @DisplayName("Deve falhar ao criar viagem com ida posterior à volta")
    void shouldThrowWhenDepartureAfterReturn() {
        assertThrows(IllegalArgumentException.class, () -> Trip.createNew(
                "Ubatuba", "Ubatuba, SP",
                OffsetDateTime.now().plusDays(5), OffsetDateTime.now().plusDays(2),
                BigDecimal.valueOf(200), 20, "Desc", "Itens", "img"
        ));
    }

    @Test
    @DisplayName("Deve falhar ao criar viagem com assentos zerados ou negativos")
    void shouldThrowWhenSeatsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Trip.createNew(
                "Ubatuba", "Ubatuba, SP",
                OffsetDateTime.now().plusDays(1), OffsetDateTime.now().plusDays(2),
                BigDecimal.valueOf(200), 0, "Desc", "Itens", "img"
        ));
    }

    @Test
    @DisplayName("Deve reservar assentos quando a viagem estiver PUBLISHED")
    void shouldReserveSeatsWhenPublished() {
        Trip trip = createValidTrip(10, TripStatus.PUBLISHED);

        trip.reserveSeats(4);

        assertEquals(6, trip.getAvailableSeats());
    }

    @Test
    @DisplayName("Deve falhar ao reservar assentos se a viagem não estiver PUBLISHED")
    void shouldThrowWhenReservingNonPublishedTrip() {
        Trip trip = createValidTrip(10, TripStatus.DRAFT);

        BusinessException ex = assertThrows(BusinessException.class, () -> trip.reserveSeats(2));
        assertTrue(ex.getMessage().contains("Não é possível reservar vagas"));
    }

    @Test
    @DisplayName("Deve falhar ao reservar quantidade menor ou igual a zero")
    void shouldThrowWhenReservingZeroOrNegativeSeats() {
        Trip trip = createValidTrip(10, TripStatus.PUBLISHED);

        assertThrows(BusinessException.class, () -> trip.reserveSeats(0));
        assertThrows(BusinessException.class, () -> trip.reserveSeats(-1));
    }

    @Test
    @DisplayName("Deve falhar ao reservar mais assentos do que o disponível")
    void shouldThrowWhenReservingMoreThanAvailableSeats() {
        Trip trip = createValidTrip(3, TripStatus.PUBLISHED);

        BusinessException ex = assertThrows(BusinessException.class, () -> trip.reserveSeats(4));
        assertTrue(ex.getMessage().contains("Vagas insuficientes"));
    }

    @Test
    @DisplayName("Deve liberar assentos com sucesso respeitando o totalSeats")
    void shouldReleaseSeatsSuccessfully() {
        Trip trip = createValidTrip(10, TripStatus.PUBLISHED);
        trip.reserveSeats(5);
        assertEquals(5, trip.getAvailableSeats());

        trip.releaseSeats(3);
        assertEquals(8, trip.getAvailableSeats());

        // Liberar além do total não pode ultrapassar totalSeats
        trip.releaseSeats(10);
        assertEquals(10, trip.getAvailableSeats());
    }

    @Test
    @DisplayName("Deve lançar exceção ao liberar assentos com valor menor ou igual a zero")
    void shouldThrowWhenReleasingInvalidSeats() {
        Trip trip = createValidTrip(10, TripStatus.PUBLISHED);

        assertThrows(BusinessException.class, () -> trip.releaseSeats(0));
    }

    @Test
    @DisplayName("Deve atualizar dados da viagem com sucesso")
    void shouldUpdateTripSuccessfully() {
        Trip trip = createValidTrip(10, TripStatus.DRAFT);

        trip.updateTrip(
                "Novo Titulo",
                OffsetDateTime.now().plusDays(2),
                OffsetDateTime.now().plusDays(4),
                BigDecimal.valueOf(400),
                15,
                "Nova desc",
                "Novos itens",
                "nova_img"
        );

        assertEquals("Novo Titulo", trip.getTitle());
        assertEquals(15, trip.getTotalSeats());
        assertEquals(15, trip.getAvailableSeats());
    }

    @Test
    @DisplayName("Deve falhar ao tentar atualizar viagem concluída ou cancelada")
    void shouldThrowWhenUpdatingCancelledTrip() {
        Trip trip = createValidTrip(10, TripStatus.CANCELLED);

        assertThrows(BusinessException.class, () -> trip.updateTrip(
                "Novo Titulo", null, null, BigDecimal.valueOf(100), 10, null, null, null
        ));
    }
}
