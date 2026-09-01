package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

import java.util.List;

public class ListarPagamentosPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepository;

    public ListarPagamentosPedidoUseCase(
            PedidoRepositoryPort pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pagamento> executar(Long pedidoId) {
        return pedidoRepository.listarPagamentos(pedidoId);
    }
}