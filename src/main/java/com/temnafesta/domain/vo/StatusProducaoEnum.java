package com.temnafesta.domain.vo;

import java.util.List;

public enum StatusProducaoEnum {
    RASCUNHO,
    AGUARDANDO_SINAL,
    CONFIRMADO,
    EM_PRODUCAO,
    PRONTO_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO;

    // Define as transições válidas de cada estado [cite: 18, 59]
    public boolean podeTransitarPara(StatusProducaoEnum novoStatus) {
        return switch (this) {
            case RASCUNHO -> List.of(AGUARDANDO_SINAL, CANCELADO).contains(novoStatus);
            case AGUARDANDO_SINAL -> List.of(CONFIRMADO, CANCELADO).contains(novoStatus);
            case CONFIRMADO -> List.of(EM_PRODUCAO, CANCELADO).contains(novoStatus);
            case EM_PRODUCAO -> List.of(PRONTO_PARA_ENTREGA, CANCELADO).contains(novoStatus);
            case PRONTO_PARA_ENTREGA -> List.of(ENTREGUE, CANCELADO).contains(novoStatus);
            case ENTREGUE, CANCELADO -> false; // Estados finais (terminais)
        };
    }
}