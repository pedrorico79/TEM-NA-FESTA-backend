package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    Optional<Cliente> buscarPorId(Long id);
    Optional<Cliente> buscarPorIdIncluindoDeletados(Long id);
    Cliente salvar(Cliente cliente);
    List<Cliente> listarNaoDeletadosPorBusca(String busca);
    default List<Cliente> listarNaoDeletadosPorBusca(String busca, boolean ativos) {
        List<Cliente> clientes = listarNaoDeletadosPorBusca(busca);
        return ativos ? clientes.stream().filter(Cliente::isAtivo).toList() : clientes;
    }
    Page<Cliente> listarNaoDeletadosPorBusca(String busca, boolean ativos, Pageable pageable);
}
