package com.temnafesta.infrastructure.dto;

import com.temnafesta.domain.vo.StatusProducaoEnum;

public record AlteracaoStatusNotificacaoEvent(
        StatusProducaoEnum status,
        String telefoneDestino
) {}
