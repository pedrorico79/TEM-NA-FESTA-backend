package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;

public class MetodoPagamento {
    private Long id;
    private String nome;

    public MetodoPagamento(Long id, String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("O nome do método de pagamento é obrigatório.");
        }
        this.id = id;
        this.nome = nome;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
}