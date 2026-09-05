package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    Optional<Cliente> buscarPorId(Long id);
    Optional<Cliente> buscarPorIdIncluindoDeletados(Long id);
    Cliente salvar(Cliente cliente);
    List<Cliente> listarNaoDeletadosPorBusca(String busca);
}