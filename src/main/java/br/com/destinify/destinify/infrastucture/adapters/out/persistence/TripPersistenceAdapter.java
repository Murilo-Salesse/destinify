package br.com.destinify.destinify.infrastucture.adapters.out.persistence;

import br.com.destinify.destinify.application.ports.out.TripRepositoryPort;
import br.com.destinify.destinify.domain.model.Trip;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.TripEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.mapper.TripPersistenceMapper;
import br.com.destinify.destinify.infrastucture.adapters.out.repository.TripRepository;
import org.springframework.stereotype.Component;

import java.util.List;
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

        // 1. Converto de domínio para entidade
        TripEntity entity = TripPersistenceMapper.toEntity(trip);

        // 2. Salvo no banco a entidade
        TripEntity savedEntity = tripRepository.save(entity);

        // 3. Devolvo para a aplicação como Domínio puro
        return TripPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Trip> findById(UUID id) {
        return tripRepository.findById(id)
                .map(TripPersistenceMapper::toDomain);
    }

    @Override
    public List<Trip> findAll() {
        return tripRepository.findAll()
                .stream()
                .map(TripPersistenceMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }
}
