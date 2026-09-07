package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.ports.repository.EventoRepositoryPort;

public class BuscarEventoPorIdUseCase {

    private final EventoRepositoryPort eventoRepositoryPort;

    public BuscarEventoPorIdUseCase(EventoRepositoryPort eventoRepositoryPort) {
        this.eventoRepositoryPort = eventoRepositoryPort;
    }

    public Evento executar(Long id) {
        if (id == null) {
            throw new RegraDeNegocioException("ID do evento é obrigatório.");
        }
        return eventoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RegraDeNegocioException("Evento não encontrado com o ID: " + id));
    }
}
