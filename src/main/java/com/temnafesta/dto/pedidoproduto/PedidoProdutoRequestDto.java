package com.temnafesta.dto.pedidoproduto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class PedidoProdutoRequestDto {

    @NotNull
    @Positive
    private Integer pedidoId;

    @NotNull
    @Positive
    private Integer produtoId;

    @NotNull
    @Positive
    private Integer quantidade;

    @NotNull
    @PositiveOrZero
    private BigDecimal precoUnitario;

    public Integer getPedidoId() {return pedidoId;}public void setPedidoId(Integer pedidoId) {this.pedidoId = pedidoId;}
    public Integer getProdutoId() {return produtoId;}public void setProdutoId(Integer produtoId) {this.produtoId = produtoId;}
    public Integer getQuantidade() {return quantidade;}public void setQuantidade(Integer quantidade) {this.quantidade = quantidade;}
    public BigDecimal getPrecoUnitario() {return precoUnitario;}public void setPrecoUnitario(BigDecimal precoUnitario) {this.precoUnitario = precoUnitario;}
}
