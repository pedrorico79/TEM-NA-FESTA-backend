package com.temnafesta.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cardapio_produto")
public class CardapioProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cardapio_id", nullable = false)
    private Cardapio cardapio;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column
    private Integer ordemExibicao;

    @Column
    private Integer qtdProdutoTotal;

    @Column
    private Integer qtdProdutoDisponivel;

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Cardapio getCardapio() {return cardapio;}
    public void setCardapio(Cardapio cardapio) {this.cardapio = cardapio;}

    public Produto getProduto() {return produto;}
    public void setProduto(Produto produto) {this.produto = produto;}

    public Integer getOrdemExibicao() {return ordemExibicao;}
    public void setOrdemExibicao(Integer ordemExibicao) {this.ordemExibicao = ordemExibicao;}

    public Integer getQtdProdutoTotal() {return qtdProdutoTotal;}
    public void setQtdProdutoTotal(Integer qtdProdutoTotal) {this.qtdProdutoTotal = qtdProdutoTotal;}

    public Integer getQtdProdutoDisponivel() {return qtdProdutoDisponivel;}
    public void setQtdProdutoDisponivel(Integer qtdProdutoDisponivel) {this.qtdProdutoDisponivel = qtdProdutoDisponivel;}
}