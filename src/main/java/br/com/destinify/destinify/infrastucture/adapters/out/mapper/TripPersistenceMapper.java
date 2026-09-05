package br.com.destinify.destinify.infrastucture.adapters.out.mapper;

import br.com.destinify.destinify.domain.model.Trip;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.TripEntity;

public class TripPersistenceMapper {

    // Domínio -> Banco (JPA)
    public static TripEntity toEntity(Trip domain) {

        if (domain == null) return null;
        return new TripEntity(
                domain.getId(),
                domain.getTitle(),
                domain.getDestination(),
                domain.getDepartureAt(),
                domain.getReturnAt(),
                domain.getPrice(),
                domain.getTotalSeats(),
                domain.getAvailableSeats(),
                domain.getDescription(),
                domain.getIncludedItems(),
                domain.getCoverImageUrl(),
                domain.getStatus(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    // Banco (JPA) -> Domínio
    public static Trip toDomain(TripEntity entity) {

        if (entity == null) return null;
        return new Trip(
                entity.getId(),
                entity.getTitle(),
                entity.getDestination(),
                entity.getDepartureAt(),
                entity.getReturnAt(),
                entity.getPrice(),
                entity.getTotalSeats(),
                entity.getAvailableSeats(),
                entity.getDescription(),
                entity.getIncludedItems(),
                entity.getCoverImageUrl(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
