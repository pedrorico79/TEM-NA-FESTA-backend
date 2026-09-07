package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.relatorio.PedidosPeriodoOutput;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class ListaPedidosPeriodoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public ListaPedidosPeriodoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public Page<PedidosPeriodoOutput> execute(LocalDate de, LocalDate ate, Pageable pageable) {
        Objects.requireNonNull(de, "data 'de' não pode ser nula");
        Objects.requireNonNull(ate, "data 'ate' não pode ser nula");
        Objects.requireNonNull(pageable, "pageable não pode ser nulo");

        if (de.isAfter(ate)) {
            throw new IllegalArgumentException("'de' não pode ser posterior a 'ate'");
        }

        LocalDateTime inicio = de.atStartOfDay();
        LocalDateTime fim = ate.atTime(23, 59, 59);

        Page<PedidosPeriodoOutput> resultado = pedidoRepositoryPort.buscarPedidosPeriodoPaginado(inicio, fim, pageable);

        // garantir retorno não-nulo
        return resultado == null ? Page.empty() : resultado;
    }
}