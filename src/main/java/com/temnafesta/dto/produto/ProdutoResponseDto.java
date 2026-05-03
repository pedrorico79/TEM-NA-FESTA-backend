package com.temnafesta.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dados do produto")
public class ProdutoResponseDto {
    @Schema(description = "ID do produto", example = "1")
    private Integer id;

    @Schema(description = "Nome do produto", example = "Bolo de Chocolate")
    private String nome;

    @Schema(description = "Descrição do produto", example = "Bolo de chocolate com cobertura de ganache")
    private String descricao;

    @Schema(description = "Preço de venda do produto", example = "49.90")
    private BigDecimal precoVenda;

    @Schema(description = "Status do produto", example = "true")
    private Boolean isAtivo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }
}
