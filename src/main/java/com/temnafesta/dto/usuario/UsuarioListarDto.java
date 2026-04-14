package com.temnafesta.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

public class UsuarioListarDto {

    @Schema(description = "Id do usuário", example = "1")
    private Integer id;

    @Schema(description = "Nome do usuário", example = "John Doe")
    private String nome;

    @Schema(description = "Email do usuário", example = "john@doe.com")
    private String email;

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
}
