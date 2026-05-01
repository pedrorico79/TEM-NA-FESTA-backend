package com.temnafesta.dto.lembrete;

import com.temnafesta.model.Perfil;
import com.temnafesta.model.Prioridade;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LembreteResponseDto {

    private Integer id;
    private String descricao;
    private LocalDate data_criacao;
    private LocalDate data_limite;
    private Prioridade prioridade;
    private UsuarioLembreteDto usuario;


    public static class UsuarioLembreteDto {


        private Integer id;
        private String nome;
        private String email;
        private String senha;
        private Perfil perfil;
        private Boolean isAtivo = true;
        private LocalDateTime dataCriacao;

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Perfil getPerfil() {
            return perfil;
        }

        public void setPerfil(Perfil perfil) {
            this.perfil = perfil;
        }

        public Boolean getAtivo() {
            return isAtivo;
        }

        public void setAtivo(Boolean ativo) {
            isAtivo = ativo;
        }

        public LocalDateTime getDataCriacao() {
            return dataCriacao;
        }

        public void setDataCriacao(LocalDateTime dataCriacao) {
            this.dataCriacao = dataCriacao;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(LocalDate data_criacao) {
        this.data_criacao = data_criacao;
    }

    public LocalDate getData_limite() {
        return data_limite;
    }

    public void setData_limite(LocalDate data_limite) {
        this.data_limite = data_limite;
    }

    public UsuarioLembreteDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioLembreteDto usuario) {
        this.usuario = usuario;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }
}
