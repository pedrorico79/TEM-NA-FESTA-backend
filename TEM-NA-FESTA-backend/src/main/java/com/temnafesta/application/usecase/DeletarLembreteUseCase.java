package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.ports.repository.LembreteRepositoryPort;

public class DeletarLembreteUseCase {

    private final LembreteRepositoryPort lembreteRepositoryPort;

    public DeletarLembreteUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        this.lembreteRepositoryPort = lembreteRepositoryPort;
    }

    public void executar(Long lembreteId, Long usuarioId) {
        var lembrete = lembreteRepositoryPort.buscarPorId(lembreteId)
                .orElseThrow(() -> new RegraDeNegocioException("Lembrete não encontrado com o ID: " + lembreteId));

        if (!lembrete.getUsuarioId().equals(usuarioId)) {
            throw new RegraDeNegocioException("Você não tem permissão para deletar este lembrete.");
        }

        lembreteRepositoryPort.deletarPorId(lembreteId);
    }
}
