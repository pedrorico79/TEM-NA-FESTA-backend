package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.infrastructure.persistence.entity.PedidoJpaEntity;
import com.temnafesta.infrastructure.persistence.mapper.PedidoPersistenceMapper;
import com.temnafesta.infrastructure.persistence.repository.SpringDataPedidoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final SpringDataPedidoRepository repository;
    private final PedidoPersistenceMapper mapper;

    public PedidoRepositoryAdapter(SpringDataPedidoRepository repository, PedidoPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        PedidoJpaEntity entity = mapper.toEntity(pedido);
        PedidoJpaEntity entitySalva = repository.save(entity);
        return mapper.toDomain(entitySalva);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Pedido> listarPorFiltros(StatusProducaoEnum status, LocalDateTime inicio, LocalDateTime fim) {
        return repository.listarPorFiltros(status, inicio, fim).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePedidoEmAndamentoPorCliente(Long clienteId) {
        return repository.existePedidoEmAndamentoPorCliente(clienteId);
    }
}