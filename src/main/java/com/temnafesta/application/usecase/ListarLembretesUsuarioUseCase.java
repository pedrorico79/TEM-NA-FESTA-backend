package com.temnafesta.application.usecase;

import com.temnafesta.domain.ports.repository.LembreteRepositoryPort;
import com.temnafesta.domain.model.Lembrete;

import java.util.List;

public class ListarLembretesUsuarioUseCase {

    private final LembreteRepositoryPort lembreteRepositoryPort;

    public ListarLembretesUsuarioUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        this.lembreteRepositoryPort = lembreteRepositoryPort;
    }

    public List<Lembrete> executar(Long usuarioId) {
        return lembreteRepositoryPort.buscarPorUsuarioId(usuarioId);
    }
}