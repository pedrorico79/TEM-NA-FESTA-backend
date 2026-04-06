package com.temnafesta.dto.pedidoproduto;

import java.time.LocalDate;

public class PedidoProdutoResponseDto {

    private Integer id;
    private PedidoDto pedido;
    private ProdutoDto produto;
    private Integer quantidade;
    private Double precoUnitario;
    private Double subtotal;

    public static class PedidoDto{
        private Integer id;
        private String nomeCliente; // único identificador útil
        private LocalDate dataEntrega;

        public String getNomeCliente() {return nomeCliente;}public void setNomeCliente(String nomeCliente) {this.nomeCliente = nomeCliente;}
        public LocalDate getDataEntrega() {return dataEntrega;}public void setDataEntrega(LocalDate dataEntrega) {this.dataEntrega = dataEntrega;}
        public Integer getId() {return id;}public void setId(Integer id) {this.id = id;}

    }

    public static class ProdutoDto{
        private Integer id;
        private String nome;

        public Integer getId() {return id;}public void setId(Integer id) {this.id = id;}
        public String getNome() {return nome;}public void setNome(String nome) {this.nome = nome;}
    }

    //Getters e Setters da Classe
    public Integer getId() {return id;}public void setId(Integer id) {this.id = id;}

    public PedidoDto getPedido() {return pedido;}public void setPedido(PedidoDto pedido) {this.pedido = pedido;}

    public ProdutoDto getProduto() {return produto;}public void setProduto(ProdutoDto produto) {this.produto = produto;}

    public Integer getQuantidade() {return quantidade;}public void setQuantidade(Integer quantidade) {this.quantidade = quantidade;}

    public Double getPrecoUnitario() {return precoUnitario;}public void setPrecoUnitario(Double precoUnitario) {this.precoUnitario = precoUnitario;}

    //Já envia calculado o subtotal de cada produto
    public Double getSubtotal() {
        if (quantidade != null && precoUnitario != null) {
            return quantidade * precoUnitario;
        }
        return 0.0;
    }

}
