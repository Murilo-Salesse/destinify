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

    public static void updateEntity(TripEntity entity, Trip domain) {
        if (entity == null || domain == null) return;

        entity.setTitle(domain.getTitle());
        entity.setDestination(domain.getDestination());
        entity.setDepartureAt(domain.getDepartureAt());
        entity.setReturnAt(domain.getReturnAt());
        entity.setPrice(domain.getPrice());
        entity.setTotalSeats(domain.getTotalSeats());
        entity.setAvailableSeats(domain.getAvailableSeats());
        entity.setDescription(domain.getDescription());
        entity.setIncludedItems(domain.getIncludedItems());
        entity.setCoverImageUrl(domain.getCoverImageUrl());
        entity.setStatus(domain.getStatus());
        // o updatedAt é atualizado automaticamente pelo @UpdateTimestamp do Hibernate
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
