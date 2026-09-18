package br.com.destinify.destinify.integration;

import br.com.destinify.destinify.AbstractIntegrationTest;
import br.com.destinify.destinify.domain.enums.DocumentType;
import br.com.destinify.destinify.domain.enums.TripStatus;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request.CreatePassengerRequest;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request.CreateReservationRequest;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.TripEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationOverbookingIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TripRepository tripRepository;

    @Test
    @DisplayName("Deve impedir reserva que ultrapassa as vagas disponíveis (prevenção de overbooking)")
    void shouldPreventOverbookingWhenSeatsAreInsufficient() {
        RestClient restClient = createRestClient();

        // Criar viagem com apenas 2 assentos disponíveis
        UUID tripId = UUID.randomUUID();
        TripEntity tripEntity = new TripEntity(
                tripId,
                "Excursão Rápida",
                "Destino Teste",
                OffsetDateTime.now().plusDays(5),
                OffsetDateTime.now().plusDays(6),
                BigDecimal.valueOf(100.00),
                2,
                2,
                "Descrição",
                "Itens",
                "http://img.png",
                TripStatus.PUBLISHED,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        tripRepository.save(tripEntity);

        // Tentar reservar 3 assentos
        CreatePassengerRequest p1 = new CreatePassengerRequest("P1", "111", DocumentType.CPF, 20, "Birigui");
        CreatePassengerRequest p2 = new CreatePassengerRequest("P2", "222", DocumentType.CPF, 21, "Birigui");
        CreatePassengerRequest p3 = new CreatePassengerRequest("P3", "333", DocumentType.CPF, 22, "Birigui");

        CreateReservationRequest overbookingRequest = new CreateReservationRequest(
                tripId,
                "Comprador Excesso",
                "excesso@email.com",
                "18999990000",
                3,
                "Embarque Central",
                List.of(p1, p2, p3)
        );

        HttpClientErrorException ex = assertThrows(
                HttpClientErrorException.class,
                () -> restClient.post()
                        .uri("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(overbookingRequest)
                        .retrieve()
                        .toBodilessEntity()
        );

        // Deve retornar status 422 (Unprocessable Content / Unprocessable Entity)
        assertEquals(422, ex.getStatusCode().value());
        assertTrue(ex.getResponseBodyAsString().contains("Vagas insuficientes"));

        // Verificar que os assentos da viagem continuam intactos (2 disponíveis)
        TripEntity reloadedTrip = tripRepository.findById(tripId).orElseThrow();
        assertEquals(2, reloadedTrip.getAvailableSeats());
    }
}
