package com.temnafesta.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dados do produto")
public record ProdutoResponseDto(
        @Schema(description = "ID do produto", example = "1")
        Long id,

        @Schema(description = "Nome do produto", example = "Bolo de Chocolate")
        String nome,

        @Schema(description = "Descrição do produto", example = "Bolo de chocolate com cobertura de ganache")
        String descricao,

        @Schema(description = "Preço de venda do produto", example = "49.90")
        BigDecimal precoVenda,

        @Schema(description = "Status do produto", example = "true")
        boolean ativo
) {}