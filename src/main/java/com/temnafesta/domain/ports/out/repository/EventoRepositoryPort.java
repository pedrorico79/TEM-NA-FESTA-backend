package com.temnafesta.domain.ports.out.repository;

import com.temnafesta.application.dto.relatorio.EventoComparativoOutput;
import com.temnafesta.application.dto.relatorio.EvolucaoEventoPeriodoOutput;
import com.temnafesta.domain.model.Evento;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventoRepositoryPort {
    List<Evento> listarEventosAtivos();
    List<Evento> listarEventosNaoDeletados();
    default List<Evento> listarEventos(boolean ativos) {
        return ativos ? listarEventosAtivos() : listarEventosNaoDeletados();
    }
    Evento salvar(Evento evento);
    Optional<Evento> buscarPorId(Long id);
    Evento atualizar(Evento evento);
    void deletar(Long id);
    Evento alterarStatus(Long id, boolean ativo);
    List<EventoComparativoOutput> buscarComparativoEventos(LocalDateTime de, LocalDateTime ate);
    List<EvolucaoEventoPeriodoOutput> buscarEvolucaoEvento(
            Long eventoId,
            LocalDateTime inicio,
            LocalDateTime fim,
            boolean agruparPorAno
    );
}
