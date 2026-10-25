package com.temnafesta.infrastructure.persistence.mapper;

import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.infrastructure.dto.TipoEventoEnum;

public final class TipoEventoMapper {

    private TipoEventoMapper() {}

    public static TipoEventoEnum from(StatusProducaoEnum status) {
        return switch (status) {
            case AGUARDANDO_SINAL -> TipoEventoEnum.PEDIDO_CRIADO;
            case CONFIRMADO -> TipoEventoEnum.PEDIDO_CONFIRMADO;
            case EM_PRODUCAO -> TipoEventoEnum.EM_PRODUCAO;
            case PRONTO_PARA_ENTREGA -> TipoEventoEnum.PRONTO_PARA_ENTREGA;
            case ENTREGUE -> TipoEventoEnum.ENTREGUE;
            case CANCELADO -> TipoEventoEnum.CANCELADO;
            case RASCUNHO -> throw new IllegalStateException(
                    "Rascunho não gera notificação"
            );
        };
    }
}