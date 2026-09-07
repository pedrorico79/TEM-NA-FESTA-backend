package com.temnafesta.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Status ativo desejado para o produto")
public record AlterarAtivoProdutoRequestDto(
        @NotNull(message = "O status 'ativo' é obrigatório.")
        @Schema(description = "Novo status do produto", example = "false")
        Boolean ativo
) {}