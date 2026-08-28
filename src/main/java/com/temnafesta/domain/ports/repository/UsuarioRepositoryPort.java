package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(String id);

    Optional<List<Usuario>> listarTodos();

    Usuario atualizar(Usuario usuario);

    void deletar(String id);

    Optional<Usuario> buscarPorEmail(String email);
}