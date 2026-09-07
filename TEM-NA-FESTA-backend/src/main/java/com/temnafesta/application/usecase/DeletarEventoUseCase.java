package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.ports.repository.EventoRepositoryPort;

public class DeletarEventoUseCase {

    private final EventoRepositoryPort eventoRepositoryPort;

    public DeletarEventoUseCase(EventoRepositoryPort eventoRepositoryPort) {
        this.eventoRepositoryPort = eventoRepositoryPort;
    }

    public void executar(Long id) {
        Evento evento = eventoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RegraDeNegocioException("Evento não encontrado com o ID: " + id));

        if (evento.isDeletado()) {
            throw new RegraDeNegocioException("Evento já foi removido.");
        }

        eventoRepositoryPort.deletar(id);
    }
}
