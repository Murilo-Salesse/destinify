package br.com.destinify.destinify.infrastucture.adapters.in.rest.dto;

import br.com.destinify.destinify.domain.enums.TripStatus;
import br.com.destinify.destinify.domain.model.Trip;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TripResponse(
        UUID id,
        String title,
        String destination,
        OffsetDateTime departureAt,
        OffsetDateTime returnAt,
        BigDecimal price,
        Integer totalSeats,
        Integer availableSeats,
        String description,
        String includedItems,
        String coverImageUrl,
        TripStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static TripResponse fromDomain(Trip trip) {
        return new TripResponse(
                trip.getId(),
                trip.getTitle(),
                trip.getDestination(),
                trip.getDepartureAt(),
                trip.getReturnAt(),
                trip.getPrice(),
                trip.getTotalSeats(),
                trip.getAvailableSeats(),
                trip.getDescription(),
                trip.getIncludedItems(),
                trip.getCoverImageUrl(),
                trip.getStatus(),
                trip.getCreatedAt(),
                trip.getUpdatedAt()
        );
    }
}