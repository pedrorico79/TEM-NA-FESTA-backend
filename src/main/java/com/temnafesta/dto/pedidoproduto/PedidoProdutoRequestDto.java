package com.temnafesta.dto.pedidoproduto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Dados para adição de produto ao pedido")
public class PedidoProdutoRequestDto {

    @Schema(description = "ID do pedido", example = "1")
    @NotNull
    @Positive
    private Integer pedidoId;

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
    @PositiveOrZero
    private BigDecimal precoUnitario;

    public Integer getPedidoId() {return pedidoId;}public void setPedidoId(Integer pedidoId) {this.pedidoId = pedidoId;}
    public Integer getProdutoId() {return produtoId;}public void setProdutoId(Integer produtoId) {this.produtoId = produtoId;}
    public Integer getQuantidade() {return quantidade;}public void setQuantidade(Integer quantidade) {this.quantidade = quantidade;}
    public BigDecimal getPrecoUnitario() {return precoUnitario;}public void setPrecoUnitario(BigDecimal precoUnitario) {this.precoUnitario = precoUnitario;}
}
