package com.temnafesta.domain.ports.out.repository;

import com.temnafesta.domain.model.MetodoPagamento;
import java.util.List;
import java.util.Optional;

public interface MetodoPagamentoRepositoryPort {
    List<MetodoPagamento> listarTodos();
    Optional<MetodoPagamento> buscarPorId(Long id);
}
