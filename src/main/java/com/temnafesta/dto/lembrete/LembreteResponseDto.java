package com.temnafesta.dto.lembrete;

import com.temnafesta.model.Perfil;
import com.temnafesta.model.Prioridade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Dados do lembrete")
public class LembreteResponseDto {

    @Schema(description = "ID do lembrete", example = "1")
    private Integer id;

    @Schema(description = "Descrição do lembrete", example = "Ligar para o cliente")
    private String descricao;

    @Schema(description = "Data de criação do lembrete", example = "2024-01-15")
    private LocalDate data_criacao;

    @Schema(description = "Data limite do lembrete", example = "2024-12-31")
    private LocalDate data_limite;

    @Schema(description = "Prioridade do lembrete", example = "ALTA")
    private Prioridade prioridade;

    @Schema(description = "Dados do usuário vinculado ao lembrete")
    private UsuarioLembreteDto usuario;


    @Schema(description = "Dados resumidos do usuário")
    public static class UsuarioLembreteDto {


        @Schema(description = "ID do usuário", example = "1")
        private Integer id;

        @Schema(description = "Nome do usuário", example = "João Silva")
        private String nome;

        @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
        private String email;

        @Schema(description = "Senha do usuário", example = "Senha@123")
        private String senha;

        @Schema(description = "Perfil do usuário", example = "ADMIN")
        private Perfil perfil;

        @Schema(description = "Status do usuário", example = "true")
        private Boolean isAtivo = true;

        @Schema(description = "Data de criação do usuário", example = "2024-01-15T14:30:00")
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
