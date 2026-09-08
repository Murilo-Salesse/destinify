package br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request;

import br.com.destinify.destinify.application.dto.request.CreateTripCommand;
import br.com.destinify.destinify.application.dto.request.UpdateTripCommand;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UpdateTripRequest(String title,

                                @Future(message = "A data de ida deve ser no futuro")
                                OffsetDateTime departureAt,

                                @Future(message = "A data de volta deve ser no futuro")
                                OffsetDateTime returnAt,

                                @Positive(message = "O preço deve ser maior que zero")
                                BigDecimal price,

                                @Positive(message = "A capacidade de assentos deve ser maior que zero")
                                Integer totalSeats,

                                String description,
                                String includedItems,
                                String coverImageUrl) {


    public UpdateTripCommand toCommand(UUID tripId) {
        return new UpdateTripCommand(
                tripId,
                title,
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
