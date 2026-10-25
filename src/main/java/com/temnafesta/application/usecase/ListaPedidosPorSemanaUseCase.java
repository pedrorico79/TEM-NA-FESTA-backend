package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.relatorio.PedidosPorSemanaOutput;
import com.temnafesta.domain.ports.out.repository.PedidoRepositoryPort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ListaPedidosPorSemanaUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public ListaPedidosPorSemanaUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public List<PedidosPorSemanaOutput> execute(LocalDate de, LocalDate ate) {
        return execute(de, ate, null);
    }

    public List<PedidosPorSemanaOutput> execute(LocalDate de, LocalDate ate, Long eventoId) {
        Objects.requireNonNull(de, "data 'de' não pode ser nula");
        Objects.requireNonNull(ate, "data 'ate' não pode ser nula");

        if (de.isAfter(ate)) {
            throw new IllegalArgumentException("'de' não pode ser posterior a 'ate'");
        }

        LocalDateTime inicio = de.atStartOfDay();
        LocalDateTime fim = ate.atTime(23, 59, 59, 999_999_999);

        List<PedidosPorSemanaOutput> resultado = pedidoRepositoryPort.buscarPedidosAgrupadosPorSemana(inicio, fim, eventoId);

        // Garantir retorno não-nulo (padronização)
        return resultado == null ? List.of() : resultado;
    }
}
