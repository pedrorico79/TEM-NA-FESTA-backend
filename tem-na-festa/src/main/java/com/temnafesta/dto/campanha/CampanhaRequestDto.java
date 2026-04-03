package com.temnafesta.dto.campanha;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDate;

public class CampanhaRequestDto {

    @NotBlank
    private String nome;

    @NotNull
    @FutureOrPresent
    private LocalDate dataInicio;

    @NotNull
    @Future
    private LocalDate dataFim;

    @NotNull
    private Boolean ativa;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    @AssertTrue(message = "A data de término deve ser depois da data de início")
    public boolean isDataFimAfterDataInicio() {
        if (dataInicio == null || dataFim == null) {
            return true;
        }
        return dataFim.isAfter(dataInicio);
    }

}