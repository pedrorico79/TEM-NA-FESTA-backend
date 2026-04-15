package com.temnafesta.dto.cardapioproduto;

import java.math.BigDecimal;

public class CardapioProdutoResponseDto {

    private Integer id;
    private CardapioDto cardapio;
    private ProdutoDto produto;
    private Integer ordemExibicao;
    private Integer qtdProdutoTotal;
    private Integer qtdProdutoDisponivel;

    //Dto montado para response de cardapio dentro de cardapioproduto
    public static class CardapioDto {
        private Integer id;

        private String nome;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    //Dto montado para response de produto dentro de cardapioproduto
    public static class ProdutoDto {
        private Integer id;
        private String nome;

        private BigDecimal precoVenda;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public BigDecimal getPrecoVenda() {
            return precoVenda;
        }

        public void setPrecoVenda(BigDecimal precoVenda) {
            this.precoVenda = precoVenda;
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

    public CardapioDto getCardapio() {
        return cardapio;
    }

    public void setCardapio(CardapioDto cardapio) {
        this.cardapio = cardapio;
    }

    public ProdutoDto getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDto produto) {
        this.produto = produto;
    }

    public Integer getOrdemExibicao() {
        return ordemExibicao;
    }

    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }

    public Integer getQtdProdutoTotal() {
        return qtdProdutoTotal;
    }

    public void setQtdProdutoTotal(Integer qtdProdutoTotal) {
        this.qtdProdutoTotal = qtdProdutoTotal;
    }

    public Integer getQtdProdutoDisponivel() {
        return qtdProdutoDisponivel;
    }

    public void setQtdProdutoDisponivel(Integer qtdProdutoDisponivel) {
        this.qtdProdutoDisponivel = qtdProdutoDisponivel;
    }
}