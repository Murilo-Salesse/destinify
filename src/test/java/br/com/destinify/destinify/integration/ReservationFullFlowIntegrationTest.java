package br.com.destinify.destinify.integration;

import br.com.destinify.destinify.AbstractIntegrationTest;
import br.com.destinify.destinify.domain.enums.DocumentType;
import br.com.destinify.destinify.domain.enums.TripStatus;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request.CreatePassengerRequest;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request.CreateReservationRequest;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.PassengerResponse;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.ReservationResponse;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.ResponseAPIDefault;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.TripResponse;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.TripEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFullFlowIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TripRepository tripRepository;

    @Test
    @DisplayName("Fluxo E2E: Criação de Viagem -> Reserva de Assentos -> Confirmação -> Embarque com Voucher")
    void shouldExecuteFullReservationAndBoardingFlow() {
        RestClient restClient = createRestClient();

        // 1. Criar Viagem no banco com status PUBLISHED diretamente para permitir reserva
        UUID tripId = UUID.randomUUID();
        TripEntity tripEntity = new TripEntity(
                tripId,
                "Excursão Florianópolis E2E",
                "Florianópolis, SC",
                OffsetDateTime.now().plusDays(15),
                OffsetDateTime.now().plusDays(20),
                BigDecimal.valueOf(650.00),
                10,
                10,
                "Praias do Sul",
                "Transporte e guia",
                "http://img.png",
                TripStatus.PUBLISHED,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        tripRepository.save(tripEntity);

        // 2. Fazer Reserva de 2 assentos
        CreatePassengerRequest p1 = new CreatePassengerRequest(
                "Lucas Alcantara", "123.456.789-00", DocumentType.CPF, 28, "Birigui"
        );
        CreatePassengerRequest p2 = new CreatePassengerRequest(
                "Mariana Alcantara", "987.654.321-11", DocumentType.CPF, 26, "Birigui"
        );
        CreateReservationRequest reservationRequest = new CreateReservationRequest(
                tripId,
                "Lucas Alcantara",
                "lucas@email.com",
                "18999991111",
                2,
                "Terminal Rodoviário",
                List.of(p1, p2)
        );

        ResponseAPIDefault<ReservationResponse> createReservationResponse = restClient.post()
                .uri("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(reservationRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        assertNotNull(createReservationResponse);
        ReservationResponse resData = createReservationResponse.data();
        UUID reservationId = resData.id();
        assertEquals("PENDING", resData.status().name());
        assertEquals(2, resData.passengers().size());

        String ticketCodeP1 = resData.passengers().get(0).ticketCode();
        assertTrue(ticketCodeP1.startsWith("DST-"));

        // 3. Verificar que as vagas da viagem diminuíram para 8
        ResponseAPIDefault<TripResponse> getTripResponse = restClient.get()
                .uri("/api/v1/trips/{id}", tripId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        assertNotNull(getTripResponse);
        assertEquals(8, getTripResponse.data().availableSeats());

        // 4. Confirmar a Reserva
        ResponseAPIDefault<ReservationResponse> confirmResponse = restClient.patch()
                .uri("/api/v1/reservations/{id}/confirm", reservationId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        assertNotNull(confirmResponse);
        assertEquals("CONFIRMED", confirmResponse.data().status().name());

        // 5. Realizar o Embarque (Board) do primeiro passageiro pelo ticketCode
        ResponseAPIDefault<PassengerResponse> boardResponse = restClient.patch()
                .uri("/api/v1/passengers/{ticketCode}/board", ticketCodeP1)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        assertNotNull(boardResponse);
        assertTrue(boardResponse.data().boarded());
        assertNotNull(boardResponse.data().boardedAt());
    }
}
