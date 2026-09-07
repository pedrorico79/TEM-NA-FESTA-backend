package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.MetodoPagamento;
import com.temnafesta.domain.ports.repository.MetodoPagamentoRepositoryPort;
import com.temnafesta.infrastructure.persistence.mapper.GeralPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataMetodoPagamentoRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MetodoPagamentoRepositoryAdapter implements MetodoPagamentoRepositoryPort {

    private final SpringDataMetodoPagamentoRepository repository;
    private final GeralPersistenceMapper mapper;

    public MetodoPagamentoRepositoryAdapter(SpringDataMetodoPagamentoRepository repository, GeralPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<MetodoPagamento> listarTodos() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}