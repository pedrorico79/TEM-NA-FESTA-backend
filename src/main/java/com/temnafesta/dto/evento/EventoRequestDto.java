package com.temnafesta.dto.evento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDate;

@Schema(description = "Dados para criação ou atualização de evento")
public class EventoRequestDto {

    @Schema(description = "Nome da evento", example = "Páscoa 2024")
    @NotBlank
    private String nome;

    @Schema(description = "Data de início da evento", example = "2024-03-01")
    @NotNull
    private LocalDate dataInicio;

    @Schema(description = "Data de término da evento", example = "2024-04-01")
    @NotNull
    @Future
    private LocalDate dataFim;

    @Schema(description = "Status da evento", example = "true")
    @NotNull
    private Boolean ativo;

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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @AssertTrue(message = "A data de término deve ser depois da data de início")
    public boolean isDataFimAfterDataInicio() {
        if (dataInicio == null || dataFim == null) {
            return true;
        }
        return dataFim.isAfter(dataInicio);
    }

}