package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Produto;
import java.util.Optional;

public interface ProdutoRepositoryPort {
    Optional<Produto> buscarPorId(Long id);
}