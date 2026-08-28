package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarUsuarioCommand;
import com.temnafesta.domain.model.Perfil;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.UsuarioRepositoryPort;

import java.time.LocalDateTime;
import java.util.Optional;

public class CriarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PedidoRepositoryPort perfilRepositoryPort;

    public CriarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort, PedidoRepositoryPort perfilRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.perfilRepositoryPort = perfilRepositoryPort;
    }

    public Usuario executar(CriarUsuarioCommand command) {

        Optional<Perfil> perfil = perfilRepositoryPort.buscarPorId(command.perfilId());

        Usuario usuario = new Usuario(
                command.id(),
                command.nome(),
                command.email(),
                command.senha(),
                true,
                false,
                LocalDateTime.now(),
                command.perfilId()
        );
        return usuarioRepositoryPort.salvar(usuario);
    }
}
