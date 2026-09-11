package br.com.destinify.destinify.infrastucture.adapters.out.persistence.trip;

import br.com.destinify.destinify.application.dto.request.TripFilterQuery;
import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.model.Trip;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.TripEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.mapper.TripPersistenceMapper;
import br.com.destinify.destinify.infrastucture.adapters.out.repository.TripRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TripPersistenceAdapter implements TripRepositoryPort {

    private final TripRepository tripRepository;

    public TripPersistenceAdapter(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public Trip save(Trip trip) {
        TripEntity entityToSave = tripRepository.findById(trip.getId())
                .map(existingEntity -> {
                    TripPersistenceMapper.updateEntity(existingEntity, trip);
                    return existingEntity;
                })
                .orElseGet(() -> TripPersistenceMapper.toEntity(trip));

        TripEntity savedEntity = tripRepository.save(entityToSave);
        return TripPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Trip> findById(UUID id) {
        return tripRepository.findById(id)
                .map(TripPersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        tripRepository.deleteById(id);
    }

    @Override
    public Page<Trip> findWithFilters(TripFilterQuery filter, Pageable pageable) {
        Specification<TripEntity> spec = TripSpecification.withFilter(filter);
        Page<TripEntity> entityPage = tripRepository.findAll(spec, pageable);

        return entityPage.map(TripPersistenceMapper::toDomain);
    }
}
