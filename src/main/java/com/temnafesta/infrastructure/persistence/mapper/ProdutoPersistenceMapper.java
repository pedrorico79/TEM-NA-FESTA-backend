package com.temnafesta.infrastructure.persistence.mapper;

import com.temnafesta.domain.model.Produto;
import com.temnafesta.infrastructure.persistence.entity.ProdutoJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoPersistenceMapper {
    Produto toDomain(ProdutoJpaEntity entity);
    ProdutoJpaEntity toEntity(Produto domain);
}