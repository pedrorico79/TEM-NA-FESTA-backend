package com.temnafesta.infrastructure.persistence.mapper;

import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.infrastructure.persistence.entity.EnderecoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.EventoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.ItemPedidoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.PagamentoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.PedidoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.ProdutoJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ProdutoPersistenceMapper.class)
public interface PedidoPersistenceMapper {

    @Mapping(target = "eventoId", source = "evento.id")
    @Mapping(target = "enderecoEntregaId", source = "enderecoEntrega.id")
    Pedido toDomain(PedidoJpaEntity entity);

    @Mapping(
            target = "evento",
            expression = "java(criarReferenciaEvento(domain.getEventoId()))"
    )
    @Mapping(
            target = "enderecoEntrega",
            expression = "java(criarReferenciaEndereco(domain.getEnderecoEntregaId()))"
    )
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

    @AfterMapping
    default void inicializarColecoes(
            PedidoJpaEntity entity,
            @MappingTarget Pedido domain) {

        if (entity.getItens() != null) {
            entity.getItens().size();
        }

        if (entity.getPagamentos() != null) {
            entity.getPagamentos().size();
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

    default EnderecoJpaEntity criarReferenciaEndereco(Long enderecoId) {
        if (enderecoId == null) {
            return null;
        }

        EnderecoJpaEntity endereco = new EnderecoJpaEntity();
        endereco.setId(enderecoId);
        return endereco;
    }
}