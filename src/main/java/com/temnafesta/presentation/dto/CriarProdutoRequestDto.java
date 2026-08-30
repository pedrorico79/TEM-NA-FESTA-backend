package com.temnafesta.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Dados para cadastro de um produto")
public record CriarProdutoRequestDto(
        @Schema(description = "Nome do produto", example = "Bolo de Chocolate")
        @NotBlank(message = "O nome do produto é obrigatório.")
        @Size(max = 100, message = "O nome do produto deve ter no máximo 100 caracteres.")
        String nome,

        @Schema(description = "Descrição do produto", example = "Bolo de chocolate com cobertura de ganache")
        String descricao,

        @Schema(description = "Preço de venda do produto", example = "49.90")
        @NotNull(message = "O preço de venda do produto é obrigatório.")
        @Positive(message = "O preço de venda do produto deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "O preço de venda deve ter até 8 dígitos inteiros e 2 casas decimais.")
        BigDecimal precoVenda,

        @Schema(description = "Status inicial do produto. Quando omitido, assume true.", example = "true", defaultValue = "true")
        Boolean ativo
) {}