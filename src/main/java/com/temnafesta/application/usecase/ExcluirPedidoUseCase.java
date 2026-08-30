package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

public class ExcluirPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public ExcluirPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public void executar(Long pedidoId) {
        Pedido pedido = pedidoRepositoryPort.buscarPorId(pedidoId)
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com o ID: " + pedidoId));

        // Aplica o Soft Delete e o status de Cancelamento
        pedido.excluirLogicamente();

        // Salva a alteração de estado no banco
        pedidoRepositoryPort.salvar(pedido);
    }
}