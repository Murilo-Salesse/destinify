package br.com.destinify.destinify.infrastucture.adapters.out.repository;

import br.com.destinify.destinify.infrastucture.adapters.out.entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PassengerRepository extends JpaRepository<PassengerEntity, UUID> {

    Optional<PassengerEntity> findByTicketCode(String ticketCode);

    List<PassengerEntity> findByReservationId(UUID reservationId);
}
