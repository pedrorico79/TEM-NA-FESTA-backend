package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.relatorio.EventoComparativoOutput;
import com.temnafesta.domain.ports.repository.EventoRepositoryPort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ListaComparativoEventosUseCase {

    private final EventoRepositoryPort eventoRepositoryPort;

    public ListaComparativoEventosUseCase(EventoRepositoryPort eventoRepositoryPort) {
        this.eventoRepositoryPort = eventoRepositoryPort;
    }

    public List<EventoComparativoOutput> execute(LocalDate de, LocalDate ate) {
        Objects.requireNonNull(de, "data 'de' não pode ser nula");
        Objects.requireNonNull(ate, "data 'ate' não pode ser nula");

        if (de.isAfter(ate)) {
            throw new IllegalArgumentException("'de' não pode ser posterior a 'ate'");
        }

        LocalDateTime inicio = de.atStartOfDay();
        LocalDateTime fim = ate.atTime(23, 59, 59, 999_999_999);

        List<EventoComparativoOutput> resultado =
                eventoRepositoryPort.buscarComparativoEventos(inicio, fim);

        return resultado == null ? List.of() : resultado;
    }
}