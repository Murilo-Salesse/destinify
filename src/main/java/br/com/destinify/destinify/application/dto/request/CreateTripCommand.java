package br.com.destinify.destinify.application.dto.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateTripCommand(
        String title,
        String destination,
        OffsetDateTime departureAt,
        OffsetDateTime returnAt,
        BigDecimal price,
        Integer totalSeats,
        String description,
        String includedItems,
        String coverImageUrl
) {}
