package com.temnafesta.infrastructure.persistence.adapter;

import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
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

    //TODO: validar necessidade de duplicidade de código para diferenciar atualizar e salver
    @Override
    public Pedido atualizar(Pedido pedido) {
        PedidoJpaEntity entity = mapper.toEntity(pedido);
        PedidoJpaEntity entityAtualizada = repository.save(entity);
        return mapper.toDomain(entityAtualizada);
    }

    @Override
    public long contarPorStatus(StatusProducaoEnum status) {
        return repository.countByStatusProducao(status);
    }

    @Override
    public List<Pedido> listarProximasRetiradas(LocalDateTime limite) {
        return repository.listarProximasRetiradas(limite).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Pedido> listarPedidos(
            String busca,
            StatusProducaoEnum status,
            Long eventoId) {

        return repository.listarPedidos(busca, status, eventoId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ItemPedido> buscarItemPorId(Long pedidoId, Long itemId) {
        return repository.findById(pedidoId)
                .flatMap(pedido -> pedido.getItens().stream()
                        .filter(item -> item.getId().equals(itemId))
                        .findFirst())
                .map(mapper::toDomain);
    }

    @Override
    public List<Pagamento> listarPagamentos(Long pedidoId) {
        return repository.findById(pedidoId)
                .map(PedidoJpaEntity::getPagamentos)
                .orElse(List.of())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}