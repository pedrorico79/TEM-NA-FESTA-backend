package com.temnafesta.dto.usuario;

import com.temnafesta.model.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização de usuário")
public class UsuarioAtualizacaoDto {

    @Schema(description = "Nome do usuário", example = "João Silva", minLength = 3, maxLength = 100)
    @NotBlank
    @Size(min = 3, max = 100)
    private String nome;

    @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Status do usuário", example = "true")
    @NotNull
    private Boolean ativo;

    @Schema(description = "Perfil do usuário", example = "ADMIN")
    @NotNull
    private Perfil perfil;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}