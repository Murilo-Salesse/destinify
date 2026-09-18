package br.com.destinify.destinify.integration;

import br.com.destinify.destinify.AbstractIntegrationTest;
import br.com.destinify.destinify.domain.enums.DocumentType;
import br.com.destinify.destinify.domain.enums.ReservationStatus;
import br.com.destinify.destinify.domain.enums.TripStatus;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request.CreatePassengerRequest;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request.CreateReservationRequest;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.ReservationResponse;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.ResponseAPIDefault;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.ReservationEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.TripEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.repository.ReservationRepository;
import br.com.destinify.destinify.infrastucture.adapters.out.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReservationExpirationMessagingIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("RabbitMQ E2E: Reserva PENDING deve expirar via DLX e devolver vagas ao ônibus automaticamente")
    void shouldExpireReservationViaRabbitMQAndRestoreSeats() {
        RestClient restClient = createRestClient();

        // 1. Criar Viagem com 5 assentos disponíveis
        UUID tripId = UUID.randomUUID();
        TripEntity tripEntity = new TripEntity(
                tripId,
                "Viagem Expiração Automática",
                "Destino TTL",
                OffsetDateTime.now().plusDays(10),
                OffsetDateTime.now().plusDays(12),
                BigDecimal.valueOf(200.00),
                5,
                5,
                "Descrição",
                "Itens",
                "http://img.png",
                TripStatus.PUBLISHED,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        tripRepository.save(tripEntity);

        // 2. Fazer reserva de 2 assentos (TTL de 1000ms configurado no AbstractIntegrationTest)
        CreatePassengerRequest p1 = new CreatePassengerRequest("Exp1", "123", DocumentType.CPF, 20, "Birigui");
        CreatePassengerRequest p2 = new CreatePassengerRequest("Exp2", "456", DocumentType.CPF, 22, "Birigui");
        CreateReservationRequest reservationRequest = new CreateReservationRequest(
                tripId,
                "Cliente Expirável",
                "expira@email.com",
                "18999997777",
                2,
                "Terminal",
                List.of(p1, p2)
        );

        ResponseAPIDefault<ReservationResponse> createResponse = restClient.post()
                .uri("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(reservationRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        assertNotNull(createResponse);
        UUID reservationId = createResponse.data().id();

        // 3. Confirmar que as vagas foram debitadas imediatamente para 3
        TripEntity intermediateTrip = tripRepository.findById(tripId).orElseThrow();
        assertEquals(3, intermediateTrip.getAvailableSeats());

        // 4. Aguardar até 10 segundos para a mensagem TTL expirar na hold queue, ser movida para a DLX, consumida e a reserva expirar
        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    ReservationEntity reservation = reservationRepository.findById(reservationId).orElse(null);
                    assertEquals(ReservationStatus.EXPIRED, reservation != null ? reservation.getStatus() : null);

                    TripEntity reloadedTrip = tripRepository.findById(tripId).orElseThrow();
                    assertEquals(5, reloadedTrip.getAvailableSeats());
                });
    }
}
