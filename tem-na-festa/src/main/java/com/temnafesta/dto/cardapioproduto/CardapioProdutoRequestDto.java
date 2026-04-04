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


    public Integer getProdutoId() {return produtoId;}

    public Integer getOrdemExibicao() {return ordemExibicao;}

    public Integer getQtdProdutoTotal() {return qtdProdutoTotal;}

    public Integer getQtdProdutoDisponivel() {return qtdProdutoDisponivel;}
}