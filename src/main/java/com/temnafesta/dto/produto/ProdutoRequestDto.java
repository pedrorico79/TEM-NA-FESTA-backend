package com.temnafesta.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados para criação ou atualização de produto")
public class ProdutoRequestDto {
    @Schema(description = "Nome do produto", example = "Bolo de Chocolate")
    @NotBlank
    private String nome;

    @Schema(description = "Descrição do produto", example = "Bolo de chocolate com cobertura de ganache")
    private String descricao;

    @Schema(description = "Preço de venda do produto", example = "49.90")
    @NotNull
    @Positive
    private BigDecimal precoVenda;

    @Schema(description = "Status do produto", example = "true")
    @NotNull
    private Boolean isAtivo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda( BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
