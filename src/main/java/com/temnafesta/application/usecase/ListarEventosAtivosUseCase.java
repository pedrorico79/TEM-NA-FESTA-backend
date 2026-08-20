package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.ports.repository.EventoRepositoryPort;

import java.util.List;

public class ListarEventosAtivosUseCase {

    private final EventoRepositoryPort eventoRepositoryPort;

    public ListarEventosAtivosUseCase(EventoRepositoryPort eventoRepositoryPort) {
        this.eventoRepositoryPort = eventoRepositoryPort;
    }

    public List<Evento> executar() {
        return eventoRepositoryPort.listarEventosAtivos();
    }
}