package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.Perfil;
import com.temnafesta.domain.ports.repository.PerfilRepositoryPort;
import com.temnafesta.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataPerfilRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PerfilRepositoryAdapter implements PerfilRepositoryPort {

    private final SpringDataPerfilRepository repository;
    private final UsuarioPersistenceMapper mapper;

    public PerfilRepositoryAdapter(SpringDataPerfilRepository repository, UsuarioPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Perfil> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}
