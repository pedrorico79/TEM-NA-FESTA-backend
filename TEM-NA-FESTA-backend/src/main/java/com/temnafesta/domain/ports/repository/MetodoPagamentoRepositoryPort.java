package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.MetodoPagamento;
import java.util.List;

public interface MetodoPagamentoRepositoryPort {
    List<MetodoPagamento> listarTodos();
}