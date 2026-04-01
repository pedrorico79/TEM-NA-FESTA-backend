package com.temnafesta.dto.perfil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PerfilRequestDto {

    @NotBlank
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
