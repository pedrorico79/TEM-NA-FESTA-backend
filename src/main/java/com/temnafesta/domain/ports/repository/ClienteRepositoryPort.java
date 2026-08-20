package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.Cliente;
import java.util.Optional;

public interface ClienteRepositoryPort {
    Optional<Cliente> buscarPorId(Long id);
}