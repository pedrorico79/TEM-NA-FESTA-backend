package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.MetodoPagamento;
import com.temnafesta.domain.ports.repository.MetodoPagamentoRepositoryPort;

import java.util.List;

public class ListarMetodosPagamentoUseCase {

    private final MetodoPagamentoRepositoryPort metodoPagamentoRepositoryPort;

    public ListarMetodosPagamentoUseCase(MetodoPagamentoRepositoryPort metodoPagamentoRepositoryPort) {
        this.metodoPagamentoRepositoryPort = metodoPagamentoRepositoryPort;
    }

    public List<MetodoPagamento> executar() {
        return metodoPagamentoRepositoryPort.listarTodos();
    }
}