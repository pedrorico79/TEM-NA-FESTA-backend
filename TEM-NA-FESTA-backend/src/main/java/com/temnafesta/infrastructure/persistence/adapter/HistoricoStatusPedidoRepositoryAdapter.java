package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.HistoricoStatusPedido;
import com.temnafesta.domain.ports.repository.HistoricoStatusPedidoRepositoryPort;
import com.temnafesta.infrastructure.persistence.entity.HistoricoStatusPedidoJpaEntity;
import com.temnafesta.infrastructure.persistence.mapper.HistoricoStatusPedidoPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataHistoricoStatusPedidoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoricoStatusPedidoRepositoryAdapter implements HistoricoStatusPedidoRepositoryPort {

    private final SpringDataHistoricoStatusPedidoRepository repository;
    private final HistoricoStatusPedidoPersistenceMapper mapper;

    public HistoricoStatusPedidoRepositoryAdapter(SpringDataHistoricoStatusPedidoRepository repository,
                                                  HistoricoStatusPedidoPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public HistoricoStatusPedido salvar(HistoricoStatusPedido historico) {
        HistoricoStatusPedidoJpaEntity entity = mapper.toEntity(historico);
        HistoricoStatusPedidoJpaEntity entitySalva = repository.save(entity);
        return mapper.toDomain(entitySalva);
    }

    @Override
    public List<HistoricoStatusPedido> buscarPorPedidoId(Long pedidoId) {
        return repository.findByPedidoId(pedidoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}