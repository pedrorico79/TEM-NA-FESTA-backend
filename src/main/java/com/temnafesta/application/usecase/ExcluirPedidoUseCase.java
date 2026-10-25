package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.out.repository.PedidoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

public class ExcluirPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public ExcluirPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    @Transactional
    public void executar(Long pedidoId) {
        Pedido pedido = pedidoRepositoryPort.buscarPorId(pedidoId)
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com o ID: " + pedidoId));

        pedido.excluirLogicamente();

        pedidoRepositoryPort.salvar(pedido);
    }
}