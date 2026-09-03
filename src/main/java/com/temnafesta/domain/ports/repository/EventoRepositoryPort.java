package com.temnafesta.domain.ports.repository;

import com.temnafesta.application.dto.relatorio.EventoComparativoOutput;
import com.temnafesta.domain.model.Evento;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepositoryPort {
    List<Evento> listarEventosAtivos();
    List<EventoComparativoOutput> buscarComparativoEventos(LocalDateTime de, LocalDateTime ate);
}