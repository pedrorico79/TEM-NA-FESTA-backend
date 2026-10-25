package com.temnafesta.infrastructure.persistence.adapter.messaging;

import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.out.PedidoEventPublisherPort;
import com.temnafesta.infrastructure.config.RabbitBeanConfiguration;
import com.temnafesta.infrastructure.dto.PedidoNotificacaoEvent;
import com.temnafesta.infrastructure.persistence.mapper.TipoEventoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RabbitMQPedidoEventPublisher implements PedidoEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQPedidoEventPublisher.class);

    public RabbitMQPedidoEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;;
    }

    @Override
    public void publicarNotificacao(Pedido pedidoSalvo, Cliente cliente) {

        PedidoNotificacaoEvent event = montarPayload(pedidoSalvo, cliente);

        try {
            rabbitTemplate.convertAndSend(
                    RabbitBeanConfiguration.EXCHANGE_PEDIDO,
                    RabbitBeanConfiguration.ROUTING_KEY,
                    event
            );
            LOGGER.info("Evento enviado a fila com sucesso.");

        } catch (AmqpException e) {
            throw new AmqpException("Erro ao chamar serviço externo", e);
        }
    }

    private PedidoNotificacaoEvent montarPayload(Pedido pedidoSalvo, Cliente cliente) {
        return new PedidoNotificacaoEvent(
                pedidoSalvo.getId(),
                TipoEventoMapper.from(pedidoSalvo.getStatusProducao()),
                Instant.now(),
                pedidoSalvo.getValorTotal(),
                pedidoSalvo.getTaxaEntrega(),
                pedidoSalvo.getObservacao(),
                cliente.getNome(),
                cliente.getWhatsapp() != null ? cliente.getWhatsapp() : cliente.getTelefone(),
                pedidoSalvo.getItens().stream()
                        .map(item -> new PedidoNotificacaoEvent.ItemPedidoEvent(
                                item.getProdutoId(),
                                item.getProduto().getNome(),
                                item.getQuantidade(),
                                item.getPrecoUnitario(),
                                item.calcularSubtotal())
                        ).toList()
        );
    }
}
