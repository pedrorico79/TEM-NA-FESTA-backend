package com.temnafesta.dto.evento;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados do evento")
public class EventoResponseDto {

    @Schema(description = "ID do evento", example = "1")
    private Integer id;

    @Schema(description = "Nome do evento", example = "Páscoa 2024")
    private String nome;

    @Schema(description = "Data de início do evento", example = "2024-03-01")
    private LocalDate dataInicio;

    @Schema(description = "Data de término do evento", example = "2024-04-01")
    private LocalDate dataFim;

    @Schema(description = "Status do evento", example = "true")
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
