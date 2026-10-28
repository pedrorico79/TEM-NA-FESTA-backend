package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.ports.out.repository.ClienteRepositoryPort;

public class BuscarClientePorIdUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;

    public BuscarClientePorIdUseCase(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    public Cliente executar(Long id) {
        return clienteRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoException(
                        "Cliente não encontrado com o ID: " + id));
    }
}
