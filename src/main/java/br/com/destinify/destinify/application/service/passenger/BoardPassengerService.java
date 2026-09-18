package br.com.destinify.destinify.application.service.passenger;

import br.com.destinify.destinify.application.ports.in.passenger.BoardPassengerUseCase;
import br.com.destinify.destinify.application.ports.out.PassengerRepositoryPort;
import br.com.destinify.destinify.application.ports.out.ReservationRepositoryPort;
import br.com.destinify.destinify.domain.enums.ReservationStatus;
import br.com.destinify.destinify.domain.exception.BusinessException;
import br.com.destinify.destinify.domain.exception.ResourceNotFoundException;
import br.com.destinify.destinify.domain.model.Passenger;
import br.com.destinify.destinify.domain.model.Reservation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardPassengerService implements BoardPassengerUseCase {

    private final PassengerRepositoryPort passengerRepositoryPort;
    private final ReservationRepositoryPort reservationRepositoryPort;

    public BoardPassengerService(PassengerRepositoryPort passengerRepositoryPort,
                                 ReservationRepositoryPort reservationRepositoryPort) {
        this.passengerRepositoryPort = passengerRepositoryPort;
        this.reservationRepositoryPort = reservationRepositoryPort;
    }

    @Override
    @Transactional
    public Passenger execute(String ticketCode) {
        Passenger passenger = passengerRepositoryPort.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Passageiro não encontrado com o bilhete: " + ticketCode));

        Reservation reservation = reservationRepositoryPort.findById(passenger.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva associada não encontrada: " + passenger.getReservationId()));

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new BusinessException("Embarque não permitido. A reserva não está confirmada (Status atual: " + reservation.getStatus().getDescription() + ").");
        }

        passenger.board();
        return passengerRepositoryPort.save(passenger);
    }
}
