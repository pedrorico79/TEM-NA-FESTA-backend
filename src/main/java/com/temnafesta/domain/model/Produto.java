package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;

import java.math.BigDecimal;

public class Produto {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal precoVenda;
    private boolean ativo = true;
    private boolean deletado = false;

    public Produto(Long id, String nome, String descricao, BigDecimal precoVenda, Boolean ativo, Boolean deletado) {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("O nome do produto é obrigatório.");
        }
        if (precoVenda == null || precoVenda.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraDeNegocioException("O preço de venda do produto deve ser maior ou igual a zero.");
        }
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.precoVenda = precoVenda;
        if (ativo != null) this.ativo = ativo;
        if (deletado != null) this.deletado = deletado;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public boolean isAtivo() { return ativo; }
    public boolean isDeletado() { return deletado; }
}