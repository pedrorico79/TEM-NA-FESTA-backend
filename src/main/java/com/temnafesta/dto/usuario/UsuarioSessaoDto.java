package com.temnafesta.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados da sessão do usuário autenticado")
public class UsuarioSessaoDto {
    @Schema(description = "ID do usuário", example = "1")
    private Integer userId;

    @Schema(description = "Nome do usuário", example = "João Silva")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
    private String email;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}