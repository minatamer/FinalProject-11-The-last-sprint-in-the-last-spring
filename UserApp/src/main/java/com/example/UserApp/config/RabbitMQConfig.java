package com.example.UserApp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MESSAGE_QUEUE = "messages_queue";
    public static final String EXCHANGE = "shared_exchange";
    public static final String MESSAGE_ROUTING_KEY = "message_routing_key";

    @Bean
    public Queue queue() {
        return new Queue(MESSAGE_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }


    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(MESSAGE_ROUTING_KEY);
    }
}
