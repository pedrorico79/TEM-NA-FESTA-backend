package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.relatorio.ProdutosMaisVendidosOutput;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class ListaProdutosMaisVendidosUseCase {

    private final ProdutoRepositoryPort produtoRepositoryPort;

    public ListaProdutosMaisVendidosUseCase(ProdutoRepositoryPort produtoRepositoryPort) {
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public Page<ProdutosMaisVendidosOutput> execute(LocalDate de, LocalDate ate, Pageable pageable) {
        Objects.requireNonNull(de, "data 'de' não pode ser nula");
        Objects.requireNonNull(ate, "data 'ate' não pode ser nula");
        Objects.requireNonNull(pageable, "pageable não pode ser nulo");

        if (de.isAfter(ate)) {
            throw new IllegalArgumentException("'de' não pode ser posterior a 'ate'");
        }

        LocalDateTime inicio = de.atStartOfDay();
        LocalDateTime fim = ate.atTime(23, 59, 59, 999_999_999);

        Page<ProdutosMaisVendidosOutput> resultado =
                produtoRepositoryPort.buscarProdutosMaisVendidosPaginado(inicio, fim, pageable);

        return resultado == null ? Page.empty() : resultado;
    }
}