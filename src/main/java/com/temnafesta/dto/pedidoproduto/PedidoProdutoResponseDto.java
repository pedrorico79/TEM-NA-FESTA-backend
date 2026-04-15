package com.temnafesta.dto.pedidoproduto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PedidoProdutoResponseDto {

    private Integer id;
    private PedidoDto pedido;
    private ProdutoDto produto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    public static class PedidoDto {
        private Integer id;
        private String nomeCliente; // único identificador útil
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

    public static class ProdutoDto {
        private Integer id;
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
