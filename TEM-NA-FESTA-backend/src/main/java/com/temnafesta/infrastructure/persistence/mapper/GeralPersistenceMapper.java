package com.temnafesta.infrastructure.persistence.mapper;

import com.temnafesta.domain.model.Evento;
import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.domain.model.MetodoPagamento;
import com.temnafesta.infrastructure.persistence.entity.EventoJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.LembreteJpaEntity;
import com.temnafesta.infrastructure.persistence.entity.MetodoPagamentoJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GeralPersistenceMapper {
    Evento toDomain(EventoJpaEntity entity);
    EventoJpaEntity toEntity(Evento domain);

    MetodoPagamento toDomain(MetodoPagamentoJpaEntity entity);
    MetodoPagamentoJpaEntity toEntity(MetodoPagamento domain);

    Lembrete toDomain(LembreteJpaEntity entity);
    LembreteJpaEntity toEntity(Lembrete domain);
}