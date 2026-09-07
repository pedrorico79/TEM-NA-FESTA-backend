package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarEventoCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.ports.repository.EventoRepositoryPort;

public class CriarEventoUseCase {

    private final EventoRepositoryPort eventoRepositoryPort;

    public CriarEventoUseCase(EventoRepositoryPort eventoRepositoryPort) {
        this.eventoRepositoryPort = eventoRepositoryPort;
    }

    public Evento executar(CriarEventoCommand command) {
        if (command == null) {
            throw new RegraDeNegocioException("Dados do evento são obrigatórios.");
        }
        if (command.nome() == null || command.nome().isBlank()) {
            throw new RegraDeNegocioException("O nome do evento é obrigatório.");
        }
        if (command.dataInicio() == null) {
            throw new RegraDeNegocioException("A data de início do evento é obrigatória.");
        }
        if (command.dataFim() == null) {
            throw new RegraDeNegocioException("A data de fim do evento é obrigatória.");
        }

        Evento evento = new Evento(null, command.nome().trim(), command.dataInicio(), command.dataFim(), true, false);
        return eventoRepositoryPort.salvar(evento);
    }
}
