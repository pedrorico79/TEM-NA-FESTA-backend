package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.domain.ports.repository.LembreteRepositoryPort;

public class BuscarLembretePorIdUseCase {

    private final LembreteRepositoryPort lembreteRepositoryPort;

    public BuscarLembretePorIdUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        this.lembreteRepositoryPort = lembreteRepositoryPort;
    }

    public Lembrete executar(Long lembreteId, Long usuarioId) {
        Lembrete lembrete = lembreteRepositoryPort.buscarPorId(lembreteId)
                .orElseThrow(() -> new RegraDeNegocioException("Lembrete não encontrado com o ID: " + lembreteId));

        if (!lembrete.getUsuarioId().equals(usuarioId)) {
            throw new RegraDeNegocioException("Você não tem permissão para visualizar este lembrete.");
        }

        return lembrete;
    }
}
