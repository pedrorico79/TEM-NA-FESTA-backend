package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;

public class BuscarClientePorIdUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;

    public BuscarClientePorIdUseCase(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    public Cliente executar(Long id) {
        return clienteRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id));
    }
}
