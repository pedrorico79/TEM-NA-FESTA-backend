package com.temnafesta.infrastructure.persistence.mapper;

import com.temnafesta.domain.model.HistoricoStatusPedido;
import com.temnafesta.infrastructure.persistence.entity.HistoricoStatusPedidoJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistoricoStatusPedidoPersistenceMapper {
    HistoricoStatusPedido toDomain(HistoricoStatusPedidoJpaEntity entity);
    HistoricoStatusPedidoJpaEntity toEntity(HistoricoStatusPedido domain);
}