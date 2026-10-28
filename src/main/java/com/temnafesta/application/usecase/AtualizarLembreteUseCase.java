package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarLembreteCommand;
import com.temnafesta.application.exception.SemPermissaoException;
import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.domain.ports.out.repository.LembreteRepositoryPort;

public class AtualizarLembreteUseCase {

    private final LembreteRepositoryPort lembreteRepositoryPort;

    public AtualizarLembreteUseCase(LembreteRepositoryPort lembreteRepositoryPort) {
        this.lembreteRepositoryPort = lembreteRepositoryPort;
    }

    public Lembrete executar(AtualizarLembreteCommand command) {
        Lembrete lembreteExistente = lembreteRepositoryPort.buscarPorId(command.id())
                .orElseThrow(() -> new NaoEncontradoException(
                        "Lembrete não encontrado com o ID: " + command.id()));

        if (!lembreteExistente.getUsuarioId().equals(command.usuarioId())) {
            throw new SemPermissaoException("Você não tem permissão para atualizar este lembrete.");
        }

        String descricao = command.descricao() == null ? null : command.descricao().trim();
        Lembrete lembreteAtualizado = new Lembrete(
                lembreteExistente.getId(),
                descricao,
                lembreteExistente.getDataCriacao(),
                command.dataLimite(),
                lembreteExistente.getUsuarioId()
        );

        return lembreteRepositoryPort.salvar(lembreteAtualizado);
    }
}