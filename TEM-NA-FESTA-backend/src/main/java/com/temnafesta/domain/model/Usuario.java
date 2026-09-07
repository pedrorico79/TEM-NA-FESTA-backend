package com.temnafesta.domain.model;

import java.time.LocalDateTime;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String senha; // Hash BCrypt
    private boolean ativo;
    private boolean deletado;
    private LocalDateTime dataCriacao;
    private Perfil perfil;

    public Usuario(Long id, String nome, String email, String senha, boolean ativo, boolean deletado, LocalDateTime dataCriacao, Perfil perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.deletado = deletado;
        this.dataCriacao = dataCriacao;
        this.perfil = perfil;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public boolean isAtivo() { return ativo; }
    public boolean isDeletado() { return deletado; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public Perfil getPerfil() { return perfil; }
}