package com.temnafesta.dto.cardapioproduto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class CardapioProdutoRequestDto {

    @NotNull
    @Positive
    private Integer cardapioId;

    @NotNull
    @Positive
    private Integer produtoId;

    @NotNull
    @PositiveOrZero
    private Integer ordemExibicao;

    @NotNull
    @PositiveOrZero
    private Integer qtdProdutoTotal;

    @NotNull
    @PositiveOrZero
    private Integer qtdProdutoDisponivel;

    public Integer getCardapioId() {return cardapioId;}

    public void setCardapioId(Integer cardapioId) {this.cardapioId = cardapioId;}

    public Integer getProdutoId() {return produtoId;}

    public void setProdutoId(Integer produtoId) {this.produtoId = produtoId;}

    public Integer getOrdemExibicao() {return ordemExibicao;}

    public void setOrdemExibicao(Integer ordemExibicao) {this.ordemExibicao = ordemExibicao;}

    public Integer getQtdProdutoTotal() {return qtdProdutoTotal;}

    public void setQtdProdutoTotal(Integer qtdProdutoTotal) {this.qtdProdutoTotal = qtdProdutoTotal;}

    public Integer getQtdProdutoDisponivel() {return qtdProdutoDisponivel;}

    public void setQtdProdutoDisponivel(Integer qtdProdutoDisponivel) {this.qtdProdutoDisponivel = qtdProdutoDisponivel;}
}