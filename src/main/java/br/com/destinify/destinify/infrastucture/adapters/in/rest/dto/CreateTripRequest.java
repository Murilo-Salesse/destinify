package br.com.destinify.destinify.infrastucture.adapters.in.rest.dto;

import br.com.destinify.destinify.application.dto.request.CreateTripCommand; // <-- NOVO IMPORT AQUI
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateTripRequest(
        @NotBlank(message = "O título é obrigatório")
        String title,

        @NotBlank(message = "O destino é obrigatório")
        String destination,

        @NotNull(message = "A data de ida é obrigatória")
        @Future(message = "A data de ida deve ser no futuro")
        OffsetDateTime departureAt,

        @NotNull(message = "A data de volta é obrigatória")
        @Future(message = "A data de volta deve ser no futuro")
        OffsetDateTime returnAt,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        BigDecimal price,

        @NotNull(message = "A capacidade total de assentos é obrigatória")
        @Positive(message = "A capacidade de assentos deve ser maior que zero")
        Integer totalSeats,

        String description,
        String includedItems,
        String coverImageUrl
) {

    public CreateTripCommand toCommand() {
        return new CreateTripCommand(
                title,
                destination,
                departureAt,
                returnAt,
                price,
                totalSeats,
                description,
                includedItems,
                coverImageUrl
        );
    }
}