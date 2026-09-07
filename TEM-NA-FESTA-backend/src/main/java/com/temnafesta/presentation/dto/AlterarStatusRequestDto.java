package com.temnafesta.presentation.dto;

import com.temnafesta.domain.vo.StatusProducaoEnum;
import jakarta.validation.constraints.NotNull;

public record AlterarStatusRequestDto(
        @NotNull(message = "O novo status é obrigatório.")
        StatusProducaoEnum novoStatus,
        String observacao
) {}