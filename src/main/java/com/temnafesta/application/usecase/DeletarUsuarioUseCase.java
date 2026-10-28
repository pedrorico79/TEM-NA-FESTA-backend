package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.out.repository.UsuarioRepositoryPort;

public class DeletarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public DeletarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public void executar(Long id) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado com o ID: " + id));

        if (usuario.isDeletado()) {
            throw new RegraDeNegocioException("Usuário já foi removido.");
        }

        usuarioRepositoryPort.deletar(id);
    }
}
