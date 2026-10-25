package com.temnafesta.domain.ports.out;

import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.Pedido;


public interface PedidoEventPublisherPort {

    void publicarNotificacao(Pedido pedido, Cliente cliente);
}
