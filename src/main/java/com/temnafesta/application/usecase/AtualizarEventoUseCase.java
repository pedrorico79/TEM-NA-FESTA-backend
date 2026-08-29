package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarEventoCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.ports.repository.EventoRepositoryPort;

public class AtualizarEventoUseCase {

    private final EventoRepositoryPort eventoRepositoryPort;

    public AtualizarEventoUseCase(EventoRepositoryPort eventoRepositoryPort) {
        this.eventoRepositoryPort = eventoRepositoryPort;
    }

    public Evento executar(Long id, AtualizarEventoCommand command) {
        Evento evento = eventoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RegraDeNegocioException("Evento não encontrado com o ID: " + id));

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

        Evento atualizado = new Evento(
                evento.getId(),
                command.nome().trim(),
                command.dataInicio(),
                command.dataFim(),
                evento.isAtivo(),
                evento.isDeletado()
        );

        return eventoRepositoryPort.atualizar(atualizado);
    }
}
