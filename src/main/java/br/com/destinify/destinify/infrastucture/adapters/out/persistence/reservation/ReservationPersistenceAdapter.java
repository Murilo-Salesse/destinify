package br.com.destinify.destinify.infrastucture.adapters.out.persistence.reservation;

import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import br.com.destinify.destinify.domain.model.Reservation;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.ReservationEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.TripEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.mapper.ReservationPersistenceMapper;
import br.com.destinify.destinify.infrastucture.adapters.out.repository.ReservationRepository;
import br.com.destinify.destinify.infrastucture.adapters.out.repository.TripRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ReservationPersistenceAdapter implements ReservationRepositoryPort {

    private final ReservationRepository reservationRepository;
    private final TripRepository tripRepository;

    public ReservationPersistenceAdapter(ReservationRepository reservationRepository, TripRepository tripRepository) {
        this.reservationRepository = reservationRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public Reservation save(Reservation reservation) {
        TripEntity tripEntity = tripRepository.findById(reservation.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Viagem não encontrada com o ID: " + reservation.getTripId()));

        ReservationEntity entityToSave = reservationRepository.findById(reservation.getId())
                .map(existingEntity -> {
                    ReservationPersistenceMapper.updateEntity(existingEntity, reservation, tripEntity);
                    return existingEntity;
                })
                .orElseGet(() -> ReservationPersistenceMapper.toEntity(reservation, tripEntity));

        ReservationEntity savedEntity = reservationRepository.save(entityToSave);
        return ReservationPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return reservationRepository.findById(id)
                .map(ReservationPersistenceMapper::toDomain);
    }
}
