package br.com.destinify.destinify.infrastucture.adapters.out.mapper;

import br.com.destinify.destinify.domain.model.Reservation;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.ReservationEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.TripEntity;

public class ReservationPersistenceMapper {

    // Domínio -> Banco (JPA)
    public static ReservationEntity toEntity(Reservation domain, TripEntity tripEntity) {
        if (domain == null) return null;
        return new ReservationEntity(
                domain.getId(),
                tripEntity,
                domain.getContactName(),
                domain.getContactEmail(),
                domain.getContactPhone(),
                domain.getTotalAmount(),
                domain.getSeatsCount(),
                domain.getBoardingLocation(),
                domain.getStatus(),
                domain.getExpiresAt(),
                domain.getConfirmedAt(),
                domain.getCreatedAt()
        );
    }

    // Banco (JPA) -> Domínio
    public static Reservation toDomain(ReservationEntity entity) {
        if (entity == null) return null;
        return new Reservation(
                entity.getId(),
                entity.getTripId(),
                entity.getContactName(),
                entity.getContactEmail(),
                entity.getContactPhone(),
                entity.getTotalAmount(),
                entity.getSeatsCount(),
                entity.getBoardingLocation(),
                entity.getStatus(),
                entity.getExpiresAt(),
                entity.getConfirmedAt(),
                entity.getCreatedAt()
        );
    }
}
