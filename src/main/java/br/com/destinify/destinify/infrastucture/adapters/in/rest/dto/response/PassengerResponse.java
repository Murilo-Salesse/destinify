package br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response;

import br.com.destinify.destinify.domain.enums.DocumentType;
import br.com.destinify.destinify.domain.model.Passenger;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PassengerResponse(
        UUID id,
        UUID reservationId,
        String fullName,
        String documentNumber,
        DocumentType documentType,
        Integer age,
        String city,
        String ticketCode,
        boolean boarded,
        OffsetDateTime boardedAt
) {
    public static PassengerResponse fromDomain(Passenger passenger) {
        if (passenger == null) return null;
        return new PassengerResponse(
                passenger.getId(),
                passenger.getReservationId(),
                passenger.getFullName(),
                passenger.getDocumentNumber(),
                passenger.getDocumentType(),
                passenger.getAge(),
                passenger.getCity(),
                passenger.getTicketCode(),
                passenger.isBoarded(),
                passenger.getBoardedAt()
        );
    }
}
