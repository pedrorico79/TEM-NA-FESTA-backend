package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Perfil;

import java.util.Optional;

public interface PerfilRepositoryPort {
    Optional<Perfil> buscarPorId(Long id);
}
