package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import java.time.LocalDate;

public class Evento {
    private Long id;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativo = true;
    private boolean deletado = false;

    public Evento(Long id, String nome, LocalDate dataInicio, LocalDate dataFim, Boolean ativo, Boolean deletado) {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("O nome do evento é obrigatório.");
        }
        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            throw new RegraDeNegocioException("A data de fim do evento não pode ser anterior à data de início.");
        }
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        if (ativo != null) this.ativo = ativo;
        if (deletado != null) this.deletado = deletado;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public boolean isAtivo() { return ativo; }
    public boolean isDeletado() { return deletado; }
}