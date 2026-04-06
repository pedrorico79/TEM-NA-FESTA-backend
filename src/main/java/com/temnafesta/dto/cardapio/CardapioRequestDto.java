package com.temnafesta.dto.cardapio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CardapioRequestDto {

    @NotBlank
    private String nome;

    private String observacoes;

    @NotNull
    private Integer campanhaId;

    // Getters e Setters
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getObservacoes() {return observacoes;}
    public void setObservacoes(String observacoes) {this.observacoes = observacoes;}

    public Integer getCampanhaId() {return campanhaId;}
    public void setCampanhaId(Integer campanhaId) {this.campanhaId = campanhaId;}
}