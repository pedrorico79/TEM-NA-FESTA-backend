package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.infrastructure.persistence.mapper.ClientePersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataClienteRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final SpringDataClienteRepository repository;
    private final ClientePersistenceMapper mapper;

    public ClienteRepositoryAdapter(SpringDataClienteRepository repository, ClientePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}