package com.temnafesta.dto.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados de um item (produto) a ser adicionado ao pedido")
public class ItemPedidoDto {

    @Schema(description = "ID do produto", example = "1")
    @NotNull
    @Positive
    private Integer produtoId;

    @Schema(description = "Quantidade do produto no pedido", example = "2")
    @NotNull
    @Positive
    private Integer quantidade;

    @Schema(description = "Preço unitário do produto no pedido", example = "49.90")
    @NotNull
    @Positive
    private BigDecimal precoUnitario;

    public Integer getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}

