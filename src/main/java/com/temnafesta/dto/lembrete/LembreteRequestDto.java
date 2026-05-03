package com.temnafesta.dto.lembrete;

import com.temnafesta.model.Prioridade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Dados para criação ou atualização de lembrete")
public class LembreteRequestDto {

    @Schema(description = "Descrição do lembrete", example = "Ligar para o cliente João", maxLength = 255)
    @NotBlank
    private String descricao;

    @Schema(description = "Data de criação do lembrete", example = "2024-01-15")
    @NotNull
    private LocalDate data_criacao;

    @Schema(description = "Data limite do lembrete", example = "2024-12-31")
    @FutureOrPresent
    private LocalDate data_limite;

    @Schema(description = "Prioridade do lembrete", example = "ALTA")
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
