package ru.rksp.samylovskikh.processor.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EVENTS_RAW_QUEUE = "events.raw";

    @Bean
    public Queue eventsRawQueue() {
        return new Queue(EVENTS_RAW_QUEUE, false);
    }
}
