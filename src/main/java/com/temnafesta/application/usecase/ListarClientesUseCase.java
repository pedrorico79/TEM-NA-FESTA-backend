package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.ports.out.repository.ClienteRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public Page<Cliente> executar(String busca, boolean ativos, Pageable pageable) {
        String filtro = busca == null ? "" : busca.trim();
        return clienteRepositoryPort.listarNaoDeletadosPorBusca(filtro, ativos, pageable);
    }
}
