package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;

import java.util.List;

public class ListarClientesUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;

    public ListarClientesUseCase(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    public List<Cliente> executar(String busca, boolean ativos) {
        String filtro = busca == null ? "" : busca.trim();
        if (!ativos) return clienteRepositoryPort.listarNaoDeletadosPorBusca(filtro);
        return clienteRepositoryPort.listarNaoDeletadosPorBusca(filtro, ativos);
    }

    public List<Cliente> executar(String busca) {
        return executar(busca, false);
    }
}
