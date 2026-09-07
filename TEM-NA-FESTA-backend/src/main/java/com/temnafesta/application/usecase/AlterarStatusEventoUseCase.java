package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.ports.repository.EventoRepositoryPort;

public class AlterarStatusEventoUseCase {

    private final EventoRepositoryPort eventoRepositoryPort;

    public AlterarStatusEventoUseCase(EventoRepositoryPort eventoRepositoryPort) {
        this.eventoRepositoryPort = eventoRepositoryPort;
    }

    public Evento executar(Long id, boolean ativo) {
        Evento evento = eventoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RegraDeNegocioException("Evento não encontrado com o ID: " + id));

        if (evento.isDeletado()) {
            throw new RegraDeNegocioException("Não é possível alterar o status de um evento removido.");
        }

        return eventoRepositoryPort.alterarStatus(id, ativo);
    }
}
