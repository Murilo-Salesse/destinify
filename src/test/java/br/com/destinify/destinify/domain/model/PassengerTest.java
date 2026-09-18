package br.com.destinify.destinify.domain.model;

import br.com.destinify.destinify.domain.enums.DocumentType;
import br.com.destinify.destinify.domain.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {

    @Test
    @DisplayName("Deve criar passageiro com ticket gerado com prefixo DST-")
    void shouldCreatePassengerWithGeneratedTicket() {
        Passenger passenger = Passenger.createNew(
                UUID.randomUUID(),
                "Juliana Paes",
                "123.456.789-00",
                DocumentType.CPF,
                30,
                "Birigui"
        );

        assertNotNull(passenger.getId());
        assertEquals("Juliana Paes", passenger.getFullName());
        assertNotNull(passenger.getTicketCode());
        assertTrue(passenger.getTicketCode().startsWith("DST-"));
        assertFalse(passenger.isBoarded());
    }

    @Test
    @DisplayName("Deve falhar ao criar passageiro sem nome, documento ou com idade negativa")
    void shouldValidateCreation() {
        UUID resId = UUID.randomUUID();

        assertThrows(BusinessException.class, () ->
                Passenger.createNew(resId, "", "123", DocumentType.CPF, 20, "Birigui"));
        assertThrows(BusinessException.class, () ->
                Passenger.createNew(resId, "Ana", "", DocumentType.CPF, 20, "Birigui"));
        assertThrows(BusinessException.class, () ->
                Passenger.createNew(resId, "Ana", "123", DocumentType.CPF, -1, "Birigui"));
    }

    @Test
    @DisplayName("Deve realizar embarque com sucesso")
    void shouldBoardSuccessfully() {
        Passenger passenger = Passenger.createNew(
                UUID.randomUUID(), "Juliana", "123", DocumentType.CPF, 30, "Birigui"
        );

        passenger.board();

        assertTrue(passenger.isBoarded());
        assertNotNull(passenger.getBoardedAt());
    }

    @Test
    @DisplayName("Deve falhar ao tentar embarcar novamente passageiro já embarcado")
    void shouldThrowWhenBoardingTwice() {
        Passenger passenger = Passenger.createNew(
                UUID.randomUUID(), "Juliana", "123", DocumentType.CPF, 30, "Birigui"
        );
        passenger.board();

        assertThrows(BusinessException.class, passenger::board);
    }
}
