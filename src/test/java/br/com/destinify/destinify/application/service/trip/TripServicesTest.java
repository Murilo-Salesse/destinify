package br.com.destinify.destinify.application.service.trip;

import br.com.destinify.destinify.application.dto.request.CreateTripCommand;
import br.com.destinify.destinify.application.dto.request.TripFilterQuery;
import br.com.destinify.destinify.application.dto.request.UpdateTripCommand;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.enums.TripStatus;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import br.com.destinify.destinify.domain.model.Trip;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServicesTest {

    @Mock
    private TripRepositoryPort tripRepositoryPort;

    @InjectMocks
    private CreateTripService createTripService;

    @InjectMocks
    private UpdateTripService updateTripService;

    @InjectMocks
    private DeleteTripService deleteTripService;

    @InjectMocks
    private FindTripByIdService findTripByIdService;

    @InjectMocks
    private SearchTripService searchTripService;

    private Trip createSampleTrip(UUID id) {
        return new Trip(
                id, "Viagem Ubatuba", "Ubatuba, SP",
                OffsetDateTime.now().plusDays(2), OffsetDateTime.now().plusDays(4),
                BigDecimal.valueOf(250), 30, 30, "Desc", "Itens", "img",
                TripStatus.PUBLISHED, OffsetDateTime.now(), OffsetDateTime.now()
        );
    }

    @Test
    @DisplayName("CreateTripService deve criar viagem com sucesso")
    void shouldCreateTrip() {
        CreateTripCommand cmd = new CreateTripCommand(
                "Viagem Paraty", "Paraty, RJ",
                OffsetDateTime.now().plusDays(5), OffsetDateTime.now().plusDays(7),
                BigDecimal.valueOf(300), 20, "Centro historico", "Guia", "img"
        );
        when(tripRepositoryPort.save(any(Trip.class))).thenAnswer(inv -> inv.getArgument(0));

        Trip result = createTripService.execute(cmd);

        assertNotNull(result);
        assertEquals("Viagem Paraty", result.getTitle());
        verify(tripRepositoryPort, times(1)).save(any(Trip.class));
    }

    @Test
    @DisplayName("FindTripByIdService deve retornar viagem por id ou lançar exceção")
    void shouldFindTripByIdOrThrow() {
        UUID id = UUID.randomUUID();
        Trip trip = createSampleTrip(id);

        when(tripRepositoryPort.findById(id)).thenReturn(Optional.of(trip));
        Trip found = findTripByIdService.execute(id);
        assertEquals(id, found.getId());

        when(tripRepositoryPort.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> findTripByIdService.execute(id));
    }

    @Test
    @DisplayName("UpdateTripService deve atualizar dados da viagem ou falhar se não existir")
    void shouldUpdateTripOrThrow() {
        UUID id = UUID.randomUUID();
        Trip trip = createSampleTrip(id);

        UpdateTripCommand cmd = new UpdateTripCommand(
                id, "Novo Titulo",
                OffsetDateTime.now().plusDays(3), OffsetDateTime.now().plusDays(5),
                BigDecimal.valueOf(350), 25, "Nova Desc", "Novos Itens", "nova_img"
        );

        when(tripRepositoryPort.findById(id)).thenReturn(Optional.of(trip));
        when(tripRepositoryPort.save(any(Trip.class))).thenAnswer(inv -> inv.getArgument(0));

        Trip updated = updateTripService.execute(cmd);
        assertEquals("Novo Titulo", updated.getTitle());

        when(tripRepositoryPort.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> updateTripService.execute(cmd));
    }

    @Test
    @DisplayName("DeleteTripService deve remover viagem ou lançar exceção se não existir")
    void shouldDeleteTripOrThrow() {
        UUID id = UUID.randomUUID();
        Trip trip = createSampleTrip(id);

        when(tripRepositoryPort.findById(id)).thenReturn(Optional.of(trip));
        doNothing().when(tripRepositoryPort).deleteById(id);

        deleteTripService.execute(id);
        verify(tripRepositoryPort, times(1)).deleteById(id);

        when(tripRepositoryPort.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> deleteTripService.execute(id));
    }

    @Test
    @DisplayName("SearchTripService deve delegar busca com filtros para o repositório")
    void shouldSearchTripsWithFilters() {
        Trip trip = createSampleTrip(UUID.randomUUID());
        TripFilterQuery filter = new TripFilterQuery("Ubatuba", null, null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Trip> expectedPage = new PageImpl<>(List.of(trip), pageable, 1);

        when(tripRepositoryPort.findWithFilters(filter, pageable)).thenReturn(expectedPage);

        Page<Trip> result = searchTripService.execute(filter, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals("Viagem Ubatuba", result.getContent().get(0).getTitle());
    }
}
