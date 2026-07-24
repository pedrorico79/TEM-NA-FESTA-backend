package com.temnafesta.dto.lembrete;

import com.temnafesta.model.Perfil;
import com.temnafesta.model.Prioridade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Dados do lembrete")
public class LembreteResponseDto {

    @Schema(description = "Identificador do lembrete", example = "1")
    private Integer id;

    @Schema(description = "Descrição do lembrete", example = "Confirmar local de entrega do pedido x")
    private String descricao;

    @Schema(description = "Data de criação do lembrete")
    private LocalDate dataCriacao;

    @Schema(description = "Data limite do lembrete")
    private LocalDate dataLimite;

    @Schema(description = "Prioridade do lembrete", example = "ALTA")
    private Prioridade prioridade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }
}