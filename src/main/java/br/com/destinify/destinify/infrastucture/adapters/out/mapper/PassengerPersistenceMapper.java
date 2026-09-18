package br.com.destinify.destinify.infrastucture.adapters.out.mapper;

import br.com.destinify.destinify.domain.model.Passenger;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.PassengerEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.ReservationEntity;

public class PassengerPersistenceMapper {

    public static PassengerEntity toEntity(Passenger domain, ReservationEntity reservationEntity) {
        if (domain == null) return null;
        return new PassengerEntity(
                domain.getId(),
                reservationEntity,
                domain.getFullName(),
                domain.getDocumentNumber(),
                domain.getDocumentType(),
                domain.getAge(),
                domain.getCity(),
                domain.getTicketCode(),
                domain.isBoarded(),
                domain.getBoardedAt()
        );
    }

    public static Passenger toDomain(PassengerEntity entity) {
        if (entity == null) return null;
        return new Passenger(
                entity.getId(),
                entity.getReservation() != null ? entity.getReservation().getId() : null,
                entity.getFullName(),
                entity.getDocumentNumber(),
                entity.getDocumentType(),
                entity.getAge(),
                entity.getCity(),
                entity.getTicketCode(),
                entity.isBoarded(),
                entity.getBoardedAt()
        );
    }

    public static void updateEntity(PassengerEntity entity, Passenger domain) {
        if (entity == null || domain == null) return;
        entity.setFullName(domain.getFullName());
        entity.setDocumentNumber(domain.getDocumentNumber());
        entity.setDocumentType(domain.getDocumentType());
        entity.setAge(domain.getAge());
        entity.setCity(domain.getCity());
        entity.setTicketCode(domain.getTicketCode());
        entity.setBoarded(domain.isBoarded());
        entity.setBoardedAt(domain.getBoardedAt());
    }
}
