package br.com.destinify.destinify.application.dto.request;

import br.com.destinify.destinify.domain.enums.TripStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TripFilterQuery(
        String destination,
        TripStatus status,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        OffsetDateTime startDate,
        OffsetDateTime endDate) {}
