package com.temnafesta.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitBeanConfiguration {

    public static final String EXCHANGE_PEDIDO = "exchange_pedido";
    public static final String QUEUE_PEDIDO = "queue_pedido";
    public static final String ROUTING_KEY = "routing_key_pedido";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_PEDIDO);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder
                .durable(QUEUE_PEDIDO)
                .build();
    }

    @Bean
    public Binding binding(
            Queue queuePedido,
            DirectExchange exchangePedido
    ) {
        return BindingBuilder
                .bind(queuePedido)
                .to(exchangePedido)
                .with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
