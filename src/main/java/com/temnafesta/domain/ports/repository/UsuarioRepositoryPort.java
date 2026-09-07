package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<List<Usuario>> listarTodos();
    Page<Usuario> listarPorNomePaginado(String nome, Pageable pageable);

    Usuario atualizar(Usuario usuario);

    void deletar(Long id);

    Optional<Usuario> buscarPorEmail(String email);
}