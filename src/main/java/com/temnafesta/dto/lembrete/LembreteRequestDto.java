package com.temnafesta.dto.lembrete;

import com.temnafesta.model.Prioridade;
import jakarta.persistence.Column;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class LembreteRequestDto {

    @NotBlank
    private String descricao;

    @NotNull
    private LocalDate data_criacao;

    @FutureOrPresent
    private LocalDate data_limite;

    @NotNull
    private Prioridade prioridade;


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

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }
}
