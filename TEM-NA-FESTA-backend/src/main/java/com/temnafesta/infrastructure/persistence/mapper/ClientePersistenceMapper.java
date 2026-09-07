package com.temnafesta.infrastructure.persistence.mapper;

import com.temnafesta.domain.model.Cliente;
import com.temnafesta.infrastructure.persistence.entity.ClienteJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientePersistenceMapper {
    Cliente toDomain(ClienteJpaEntity entity);
    ClienteJpaEntity toEntity(Cliente domain);
}