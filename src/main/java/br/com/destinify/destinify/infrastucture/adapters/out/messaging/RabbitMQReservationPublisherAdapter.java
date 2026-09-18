package br.com.destinify.destinify.infrastucture.adapters.out.messaging;

import br.com.destinify.destinify.application.dto.event.ReservationCreatedEvent;
import br.com.destinify.destinify.application.ports.out.ReservationEventPublisherPort;
import br.com.destinify.destinify.infrastucture.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQReservationPublisherAdapter implements ReservationEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQReservationPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishReservationCreated(ReservationCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RESERVATION_EXCHANGE,
                RabbitMQConfig.RESERVATION_HOLD_ROUTING_KEY,
                event
        );
    }
}
