package com.temnafesta.infrastructure.persistence.adapter.messaging;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.out.PedidoEventPublisherPort;
import com.temnafesta.infrastructure.config.RabbitBeanConfiguration;
import com.temnafesta.infrastructure.dto.PedidoCriadoEvent;
import com.temnafesta.infrastructure.exception.FalhaPublicacaoPedidoException;
import com.temnafesta.infrastructure.persistence.mapper.PedidoEventMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQPedidoEventPublisher implements PedidoEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private final PedidoEventMapper mapper;

    public RabbitMQPedidoEventPublisher(RabbitTemplate rabbitTemplate, PedidoEventMapper pedidoEventMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = pedidoEventMapper;
    }

    @Override
    public void publicarPedidoCriado(Pedido pedido) {
        try {
            PedidoCriadoEvent event = mapper.toEvent(pedido);

            rabbitTemplate.convertAndSend(
                    RabbitBeanConfiguration.EXCHANGE_PEDIDO,
                    RabbitBeanConfiguration.ROUTING_KEY,
                    event
            );
            System.out.println("Evento enviado a fila com sucesso");

        } catch (Exception error) {
            throw new FalhaPublicacaoPedidoException("Erro ao publicar evento", error);
        }
    }
}
