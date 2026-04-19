package com.temnafesta.dto.pedidoproduto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Dados do produto no pedido")
public class PedidoProdutoResponseDto {

    @Schema(description = "ID do registro", example = "1")
    private Integer id;

    @Schema(description = "Dados resumidos do pedido")
    private PedidoDto pedido;

    @Schema(description = "Dados resumidos do produto")
    private ProdutoDto produto;

    @Schema(description = "Quantidade do produto no pedido", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço unitário do produto no pedido", example = "49.90")
    private BigDecimal precoUnitario;

    @Schema(description = "Subtotal do produto no pedido", example = "99.80")
    private BigDecimal subtotal;

    @Schema(description = "Dados resumidos do pedido")
    public static class PedidoDto {
        @Schema(description = "ID do pedido", example = "1")
        private Integer id;

        @Schema(description = "Nome do cliente do pedido", example = "João Silva")
        private String nomeCliente; // único identificador útil

        @Schema(description = "Data e hora de entrega do pedido", example = "2024-12-31T18:00:00")
        private LocalDateTime dataEntrega;

        public String getNomeCliente() {
            return nomeCliente;
        }

        public void setNomeCliente(String nomeCliente) {
            this.nomeCliente = nomeCliente;
        }

        public LocalDateTime getDataEntrega() {
            return dataEntrega;
        }

        public void setDataEntrega(LocalDateTime dataEntrega) {
            this.dataEntrega = dataEntrega;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }

    @Schema(description = "Dados resumidos do produto")
    public static class ProdutoDto {
        @Schema(description = "ID do produto", example = "1")
        private Integer id;

        @Schema(description = "Nome do produto", example = "Bolo de Chocolate")
        private String nome;

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
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PedidoDto getPedido() {
        return pedido;
    }

    public void setPedido(PedidoDto pedido) {
        this.pedido = pedido;
    }

    public ProdutoDto getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDto produto) {
        this.produto = produto;
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

    //Já envia calculado o subtotal de cada produto
    public BigDecimal getSubtotal() {
        if (quantidade != null && precoUnitario != null) {
            return BigDecimal.valueOf(quantidade).multiply(precoUnitario);
        }
        return BigDecimal.ZERO;
    }
}
