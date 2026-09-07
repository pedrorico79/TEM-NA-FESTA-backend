package com.temnafesta.infrastructure.persistence.mapper;

import com.temnafesta.domain.model.Perfil;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.infrastructure.persistence.entity.PerfilJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioPersistenceMapper {

    Usuario toDomain(UsuarioJpaEntity entity);
    Perfil toDomain(PerfilJpaEntity entity);

    UsuarioJpaEntity toEntity(Usuario domain);
    PerfilJpaEntity toEntity(Perfil domain);
}