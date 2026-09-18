package br.com.destinify.destinify.infrastucture.adapters.in.messaging;

import br.com.destinify.destinify.application.dto.event.ReservationCreatedEvent;
import br.com.destinify.destinify.application.ports.in.reservation.ExpireReservationUseCase;
import br.com.destinify.destinify.infrastucture.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationExpirationConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReservationExpirationConsumer.class);

    private final ExpireReservationUseCase expireReservationUseCase;

    public ReservationExpirationConsumer(ExpireReservationUseCase expireReservationUseCase) {
        this.expireReservationUseCase = expireReservationUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.RESERVATION_EXPIRATION_QUEUE)
    public void consumeExpiration(ReservationCreatedEvent event) {
        log.info("Mensagem de expiração recebida da Dead Letter Queue para a reserva: {}", event.reservationId());
        expireReservationUseCase.execute(event.reservationId());
    }
}
