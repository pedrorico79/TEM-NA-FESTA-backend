package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.relatorio.PedidosPeriodoOutput;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ListaRelatorioDinamicoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public ListaRelatorioDinamicoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public List<PedidosPeriodoOutput> execute(Integer eventoId, LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("'dataInicio' não pode ser posterior a 'dataFim'");
        }

        LocalDateTime inicio = dataInicio != null ? dataInicio.atStartOfDay() : null;
        LocalDateTime fim = dataFim != null ? dataFim.atTime(23, 59, 59, 999_999_999) : null;

        List<PedidosPeriodoOutput> resultado =
                pedidoRepositoryPort.buscarRelatorioDinamico(eventoId, inicio, fim);

        return resultado == null ? List.of() : resultado;
    }
}