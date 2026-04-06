package com.temnafesta.dto.cardapio;

import jakarta.validation.constraints.NotNull;

public class CardapioAtivoDto {

    @NotNull
    private Boolean ativo;

    public Boolean getAtivo() {return ativo;}
    public void setAtivo(Boolean ativo) {this.ativo = ativo;}
}