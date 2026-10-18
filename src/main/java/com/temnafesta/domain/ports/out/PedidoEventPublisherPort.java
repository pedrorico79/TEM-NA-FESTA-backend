package com.temnafesta.domain.ports.out;

import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.infrastructure.dto.PedidoNotificacaoEvent;


public interface PedidoEventPublisherPort {

    void publicarPedidoCriado(Pedido pedido, Cliente cliente);
}
