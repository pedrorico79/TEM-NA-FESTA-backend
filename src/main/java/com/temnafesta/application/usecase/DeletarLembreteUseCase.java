package com.temnafesta.application.usecase;

import com.temnafesta.application.exception.SemPermissaoException;
import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.domain.ports.out.repository.LembreteRepositoryPort;

public class DeletarLembreteUseCase {

    private final LembreteRepositoryPort lembreteRepositoryPort;

    public DeletarLembreteUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        this.lembreteRepositoryPort = lembreteRepositoryPort;
    }

    public void executar(Long lembreteId, Long usuarioId) {
        var lembrete = lembreteRepositoryPort.buscarPorId(lembreteId)
                .orElseThrow(() -> new NaoEncontradoException("Lembrete não encontrado com o ID: " + lembreteId));

        if (!lembrete.getUsuarioId().equals(usuarioId)) {
            throw new SemPermissaoException("Você não tem permissão para deletar este lembrete.");
        }

        lembreteRepositoryPort.deletarPorId(lembreteId);
    }
}
