package br.com.destinify.destinify.infrastucture.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String RESERVATION_EXCHANGE = "destinify.reservation.exchange";
    public static final String RESERVATION_HOLD_QUEUE = "destinify.reservation.hold.queue";
    public static final String RESERVATION_HOLD_ROUTING_KEY = "reservation.created";

    public static final String RESERVATION_DLX_EXCHANGE = "destinify.reservation.dlx";
    public static final String RESERVATION_EXPIRATION_QUEUE = "destinify.reservation.expiration.queue";
    public static final String RESERVATION_EXPIRATION_ROUTING_KEY = "reservation.expired";

    @Value("${destinify.reservations.ttl-milliseconds:900000}")
    private int ttlMilliseconds;

    @Bean
    public DirectExchange reservationExchange() {
        return new DirectExchange(RESERVATION_EXCHANGE);
    }

    @Bean
    public DirectExchange reservationDlxExchange() {
        return new DirectExchange(RESERVATION_DLX_EXCHANGE);
    }

    @Bean
    public Queue reservationHoldQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", RESERVATION_DLX_EXCHANGE);
        args.put("x-dead-letter-routing-key", RESERVATION_EXPIRATION_ROUTING_KEY);
        args.put("x-message-ttl", ttlMilliseconds);
        return new Queue(RESERVATION_HOLD_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue reservationExpirationQueue() {
        return new Queue(RESERVATION_EXPIRATION_QUEUE, true);
    }

    @Bean
    public Binding holdQueueBinding(Queue reservationHoldQueue, DirectExchange reservationExchange) {
        return BindingBuilder.bind(reservationHoldQueue)
                .to(reservationExchange)
                .with(RESERVATION_HOLD_ROUTING_KEY);
    }

    @Bean
    public Binding expirationQueueBinding(Queue reservationExpirationQueue, DirectExchange reservationDlxExchange) {
        return BindingBuilder.bind(reservationExpirationQueue)
                .to(reservationDlxExchange)
                .with(RESERVATION_EXPIRATION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
