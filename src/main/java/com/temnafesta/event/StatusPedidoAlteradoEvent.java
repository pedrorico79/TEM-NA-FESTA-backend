package com.temnafesta.event;

import com.temnafesta.model.Pedido;
import com.temnafesta.model.StatusProducao;
import com.temnafesta.model.Usuario;

public class StatusPedidoAlteradoEvent {

    private final Pedido pedido;
    private final StatusProducao statusAnterior;
    private final Usuario usuario;

    public StatusPedidoAlteradoEvent(Pedido pedido, StatusProducao statusAnterior, Usuario usuario) {
        this.pedido = pedido;
        this.statusAnterior = statusAnterior;
        this.usuario = usuario;
    }

    public Pedido getPedido() { return pedido; }
    public StatusProducao getStatusAnterior() { return statusAnterior; }
    public Usuario getUsuario() { return usuario; }
}