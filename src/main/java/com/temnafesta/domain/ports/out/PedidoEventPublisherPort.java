package com.temnafesta.domain.ports.out;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.infrastructure.dto.PedidoCriadoEvent;


public interface PedidoEventPublisherPort {

    void publicarPedidoCriado(Pedido pedido);
}
