package br.com.destinify.destinify.infrastucture.adapters.out.persistence.passenger;

import br.com.destinify.destinify.application.ports.out.PassengerRepositoryPort;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import br.com.destinify.destinify.domain.model.Passenger;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.PassengerEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.ReservationEntity;
import br.com.destinify.destinify.infrastucture.adapters.out.mapper.PassengerPersistenceMapper;
import br.com.destinify.destinify.infrastucture.adapters.out.repository.PassengerRepository;
import br.com.destinify.destinify.infrastucture.adapters.out.repository.ReservationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PassengerPersistenceAdapter implements PassengerRepositoryPort {

    private final PassengerRepository passengerRepository;
    private final ReservationRepository reservationRepository;

    public PassengerPersistenceAdapter(PassengerRepository passengerRepository, ReservationRepository reservationRepository) {
        this.passengerRepository = passengerRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Passenger save(Passenger passenger) {
        ReservationEntity reservationEntity = reservationRepository.findById(passenger.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com o ID: " + passenger.getReservationId()));

        PassengerEntity entityToSave = passengerRepository.findById(passenger.getId())
                .map(existingEntity -> {
                    PassengerPersistenceMapper.updateEntity(existingEntity, passenger);
                    return existingEntity;
                })
                .orElseGet(() -> PassengerPersistenceMapper.toEntity(passenger, reservationEntity));

        PassengerEntity savedEntity = passengerRepository.save(entityToSave);
        return PassengerPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Passenger> findByTicketCode(String ticketCode) {
        return passengerRepository.findByTicketCode(ticketCode)
                .map(PassengerPersistenceMapper::toDomain);
    }

    @Override
    public List<Passenger> findByReservationId(UUID reservationId) {
        return passengerRepository.findByReservationId(reservationId)
                .stream()
                .map(PassengerPersistenceMapper::toDomain)
                .toList();
    }
}
