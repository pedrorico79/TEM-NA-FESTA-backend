package com.temnafesta.application.usecase;

import com.temnafesta.application.exception.RequisicaoInvalidaException;
import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.ports.out.repository.EventoRepositoryPort;

public class BuscarEventoPorIdUseCase {

    private final EventoRepositoryPort eventoRepositoryPort;

    public BuscarEventoPorIdUseCase(EventoRepositoryPort eventoRepositoryPort) {
        this.eventoRepositoryPort = eventoRepositoryPort;
    }

    public Evento executar(Long id) {
        if (id == null) {
            throw new RequisicaoInvalidaException("ID do evento é obrigatório.");
        }
        return eventoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("Evento não encontrado com o ID: " + id));
    }
}
