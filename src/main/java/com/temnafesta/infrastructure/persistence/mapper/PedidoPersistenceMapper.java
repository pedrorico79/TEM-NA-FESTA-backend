package com.temnafesta.infrastructure.persistence.mapper;

import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.infrastructure.persistence.entity.ItemPedidoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.PagamentoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.PedidoJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoPersistenceMapper {

    Pedido toDomain(PedidoJpaEntity entity);

    PedidoJpaEntity toEntity(Pedido domain);

    ItemPedido toDomain(ItemPedidoJpaEntity entity);

    ItemPedidoJpaEntity toEntity(ItemPedido domain);

    Pagamento toDomain(PagamentoJpaEntity entity);

    PagamentoJpaEntity toEntity(Pagamento domain);

    // Garante o relacionamento bidirecional entre Pedido e seus Filhos (Itens/Pagamentos)
    @AfterMapping
    default void vincularFilhosAoPedido(Pedido domain, @org.mapstruct.MappingTarget PedidoJpaEntity entity) {
        if (entity.getItens() != null) {
            entity.getItens().forEach(item -> item.setPedido(entity));
        }
        if (entity.getPagamentos() != null) {
            entity.getPagamentos().forEach(pagamento -> pagamento.setPedido(entity));
        }
    }
}