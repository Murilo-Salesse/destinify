package br.com.destinify.destinify.application.dto.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UpdateTripCommand(
        UUID tripId,
        String title,
        OffsetDateTime departureAt,
        OffsetDateTime returnAt,
        BigDecimal price,
        Integer totalSeats,
        String description,
        String includedItems,
        String coverImageUrl
) {}
