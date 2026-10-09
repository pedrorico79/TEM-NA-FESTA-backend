package com.temnafesta.infrastructure.persistence.mapper;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.infrastructure.dto.PedidoCriadoEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoEventMapper {

    PedidoCriadoEvent toEvent(Pedido pedido);

}
