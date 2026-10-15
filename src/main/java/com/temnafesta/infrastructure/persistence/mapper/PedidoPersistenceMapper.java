package com.temnafesta.infrastructure.persistence.mapper;

import com.temnafesta.domain.model.Endereco;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.infrastructure.persistence.entity.EnderecoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.EventoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.ItemPedidoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.PagamentoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.PedidoJpaEntity;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ProdutoPersistenceMapper.class)
public interface PedidoPersistenceMapper {

    @Mapping(target = "eventoId", source = "evento.id")
    @Mapping(target = "ativo", source = "ativo")
    @Mapping(target = "deletado", source = "deletado")
    Pedido toDomain(PedidoJpaEntity entity);

    Endereco toDomain(EnderecoJpaEntity entity);

    @Mapping(
            target = "evento",
            expression = "java(criarReferenciaEvento(domain.getEventoId()))"
    )
    @Mapping(
            target = "enderecoEntrega",
            source = "enderecoEntrega"
    )
    @Mapping(target = "ativo", source = "ativo")
    @Mapping(target = "deletado", source = "deletado")
    PedidoJpaEntity toEntity(Pedido domain);

    @Mapping(target = "produtoId", source = "produto.id")
    ItemPedido toDomain(ItemPedidoJpaEntity entity);

    ItemPedidoJpaEntity toEntity(ItemPedido domain);

    Pagamento toDomain(PagamentoJpaEntity entity);

    PagamentoJpaEntity toEntity(Pagamento domain);

    @AfterMapping
    default void vincularFilhosAoPedido(
            Pedido domain,
            @MappingTarget PedidoJpaEntity entity) {

        if (entity.getItens() != null) {
            entity.getItens().forEach(item -> item.setPedido(entity));
        }

        if (entity.getPagamentos() != null) {
            entity.getPagamentos().forEach(pagamento -> pagamento.setPedido(entity));
        }
    }

default EventoJpaEntity criarReferenciaEvento(Long eventoId) {
        if (eventoId == null) {
            return null;
        }

        EventoJpaEntity evento = new EventoJpaEntity();
        evento.setId(eventoId);
        return evento;
    }
}