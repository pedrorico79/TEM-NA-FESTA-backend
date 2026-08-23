package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;

import java.time.LocalDate;

public class Cliente {
    private Long id;
    private String nome;
    private String telefone;
    private String whatsapp;
    private String instagram;
    private LocalDate dataCadastro;
    private String anotacoes;
    private Endereco endereco; // Endereço residencial/padrão
    private boolean ativo = true;
    private boolean deletado = false;

    public Cliente(Long id, String nome, String telefone, String whatsapp, String instagram,
                   LocalDate dataCadastro, String anotacoes, Endereco endereco, Boolean ativo, Boolean deletado) {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("O nome do cliente é obrigatório.");
        }
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.whatsapp = whatsapp;
        this.instagram = instagram;
        this.dataCadastro = dataCadastro != null ? dataCadastro : LocalDate.now();
        this.anotacoes = anotacoes;
        this.endereco = endereco;
        if (ativo != null) this.ativo = ativo;
        if (deletado != null) this.deletado = deletado;
    }


    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getWhatsapp() { return whatsapp; }
    public String getInstagram() { return instagram; }
    public LocalDate getDataCadastro() { return dataCadastro; }
    public String getAnotacoes() { return anotacoes; }
    public Endereco getEndereco() { return endereco; }
    public boolean isAtivo() { return ativo; }
    public boolean isDeletado() { return deletado; }
}