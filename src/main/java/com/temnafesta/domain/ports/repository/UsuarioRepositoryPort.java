package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Usuario;
import java.util.Optional;

public interface UsuarioRepositoryPort {
    Optional<Usuario> buscarPorEmail(String email);
}