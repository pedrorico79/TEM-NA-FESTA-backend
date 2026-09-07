package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

public class ListarItemPedidoPorIdUseCase {

    private final PedidoRepositoryPort pedidoRepository;

    public ListarItemPedidoPorIdUseCase(
            PedidoRepositoryPort pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public ItemPedido executar(Long pedidoId, Long itemId) {

        return pedidoRepository.buscarItemPorId(pedidoId, itemId)
                .orElseThrow(() -> new RuntimeException(
                        "Item não encontrado no pedido informado."
                ));
    }
}