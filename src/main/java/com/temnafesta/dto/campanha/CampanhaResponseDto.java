package com.temnafesta.dto.campanha;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados da campanha")
public class CampanhaResponseDto {

    @Schema(description = "ID da campanha", example = "1")
    private Integer id;

    @Schema(description = "Nome da campanha", example = "Páscoa 2024")
    private String nome;

    @Schema(description = "Data de início da campanha", example = "2024-03-01")
    private LocalDate dataInicio;

    @Schema(description = "Data de término da campanha", example = "2024-04-01")
    private LocalDate dataFim;

    @Schema(description = "Status da campanha", example = "true")
    private Boolean ativa = false;

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
}
