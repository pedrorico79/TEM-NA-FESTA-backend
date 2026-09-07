package com.temnafesta.presentation.dto;

import com.temnafesta.domain.vo.StatusProducaoEnum;

import java.time.LocalDateTime;

public record HistoricoStatusPedidoResponseDto(
        Long id,
        LocalDateTime dataAlteracao,
        String observacao,
        StatusProducaoEnum statusProducao,
        Long pedidoId,
        Long usuarioId
) {}
