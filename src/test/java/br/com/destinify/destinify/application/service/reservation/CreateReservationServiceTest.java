package br.com.destinify.destinify.application.service.reservation;

import br.com.destinify.destinify.application.dto.event.ReservationCreatedEvent;
import br.com.destinify.destinify.application.dto.request.CreatePassengerCommand;
import br.com.destinify.destinify.application.dto.request.CreateReservationCommand;
import br.com.destinify.destinify.application.ports.out.ReservationEventPublisherPort;
import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.enums.DocumentType;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateReservationServiceTest {

    @Mock
    private ReservationRepositoryPort reservationRepositoryPort;

    @Mock
    private TripRepositoryPort tripRepositoryPort;

    @Mock
    private ReservationEventPublisherPort reservationEventPublisherPort;

    @InjectMocks
    private CreateReservationService createReservationService;

    @Test
    @DisplayName("Deve criar reserva, decrementar assentos e publicar evento no RabbitMQ")
    void shouldCreateReservationSuccessfully() {
        UUID tripId = UUID.randomUUID();
        Trip trip = new Trip(
                tripId, "Viagem Ubatuba", "Ubatuba, SP",
                OffsetDateTime.now().plusDays(3), OffsetDateTime.now().plusDays(5),
                BigDecimal.valueOf(250.00), 10, 10, "Praia e sol", "Transporte",
                "http://img.png", TripStatus.PUBLISHED,
                OffsetDateTime.now(), OffsetDateTime.now()
        );

        CreatePassengerCommand passengerCmd = new CreatePassengerCommand(
                "Carlos Souza", "111.222.333-44", DocumentType.CPF, 35, "Birigui"
        );
        CreateReservationCommand command = new CreateReservationCommand(
                tripId, "Carlos Souza", "carlos@email.com", "18999991111", 1, "Terminal", List.of(passengerCmd)
        );

        when(tripRepositoryPort.findById(tripId)).thenReturn(Optional.of(trip));
        when(reservationRepositoryPort.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = createReservationService.execute(command);

        assertNotNull(result);
        assertEquals(ReservationStatus.PENDING, result.getStatus());
        assertEquals(BigDecimal.valueOf(250.00), result.getTotalAmount());
        assertEquals(9, trip.getAvailableSeats());

        verify(tripRepositoryPort, times(1)).save(trip);
        verify(reservationRepositoryPort, times(1)).save(any(Reservation.class));
        verify(reservationEventPublisherPort, times(1)).publishReservationCreated(any(ReservationCreatedEvent.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando a viagem não existir")
    void shouldThrowExceptionWhenTripNotFound() {
        UUID tripId = UUID.randomUUID();
        CreateReservationCommand command = new CreateReservationCommand(
                tripId, "Carlos Souza", "carlos@email.com", "18999991111", 1, "Terminal", List.of()
        );

        when(tripRepositoryPort.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> createReservationService.execute(command));
        verify(reservationRepositoryPort, never()).save(any());
        verify(reservationEventPublisherPort, never()).publishReservationCreated(any());
    }
}
