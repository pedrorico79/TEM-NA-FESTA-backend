package com.temnafesta.domain.ports.repository;

import com.temnafesta.application.dto.relatorio.ProdutosMaisVendidosOutput;
import com.temnafesta.domain.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProdutoRepositoryPort {
    Optional<Produto> buscarPorId(Long id);

    Page<ProdutosMaisVendidosOutput> buscarProdutosMaisVendidosPaginado(
            LocalDateTime de,
            LocalDateTime ate,
            Pageable pageable
    );
}