package br.com.destinify.destinify.infrastucture.adapters.out.repository;

import br.com.destinify.destinify.infrastucture.adapters.out.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID>, JpaSpecificationExecutor<ReservationEntity> {
}
