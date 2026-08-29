package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.vo.StatusProducaoEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepositoryPort {
    Pedido salvar(Pedido pedido);

    Optional<Pedido> buscarPorId(Long id);

    List<Pedido> listarPorFiltros(
            StatusProducaoEnum status,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    // O adaptador desta porta no JPA deverá buscar pedidos do cliente cujo
    // status_producao_id represente algo em andamento (diferente de ENTREGUE ou CANCELADO).
    boolean existePedidoEmAndamentoPorCliente(Long clienteId);

    Pedido atualizar(Pedido pedido);
}