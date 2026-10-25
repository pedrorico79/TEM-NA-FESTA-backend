package com.temnafesta.domain.ports.out.repository;

import com.temnafesta.domain.model.Perfil;

import java.util.Optional;

public interface PerfilRepositoryPort {
    Optional<Perfil> buscarPorId(Long id);
}
