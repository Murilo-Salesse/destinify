package br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response;

import br.com.destinify.destinify.domain.enums.ReservationStatus;
import br.com.destinify.destinify.domain.model.Reservation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID tripId,
        String contactName,
        String contactEmail,
        String contactPhone,
        BigDecimal totalAmount,
        Integer seatsCount,
        String boardingLocation,
        ReservationStatus status,
        OffsetDateTime expiresAt,
        OffsetDateTime createdAt
) {

    public static ReservationResponse fromDomain(Reservation reservation){
        return new ReservationResponse(
                reservation.getId(),
                reservation.getTripId(),
                reservation.getContactName(),
                reservation.getContactEmail(),
                reservation.getContactPhone(),
                reservation.getTotalAmount(),
                reservation.getSeatsCount(),
                reservation.getBoardingLocation(),
                reservation.getStatus(),
                reservation.getExpiresAt(),
                reservation.getCreatedAt()
        );
    }
}
