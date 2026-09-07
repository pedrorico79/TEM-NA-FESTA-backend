package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.infrastructure.persistence.mapper.ClientePersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataClienteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
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
        return repository.findByIdAndDeletadoFalse(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorIdIncluindoDeletados(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        return mapper.toDomain(repository.save(mapper.toEntity(cliente)));
    }

    @Override
    public List<Cliente> listarNaoDeletadosPorBusca(String busca) {
        return repository.buscarClientesNaoDeletados(busca)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}