package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import java.time.LocalDate;

public class Lembrete {
    private Long id;
    private String descricao;
    private LocalDate dataCriacao;
    private LocalDate dataLimite;
    private Long usuarioId;

    public Lembrete(Long id, String descricao, LocalDate dataCriacao, LocalDate dataLimite, Long usuarioId) {
        if (descricao == null || descricao.isBlank()) {
            throw new RegraDeNegocioException("A descrição do lembrete é obrigatória.");
        }
        this.id = id;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao != null ? dataCriacao : LocalDate.now();
        this.dataLimite = dataLimite;
        this.usuarioId = usuarioId;
    }

    // Getters
    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public LocalDate getDataCriacao() { return dataCriacao; }
    public LocalDate getDataLimite() { return dataLimite; }
    public Long getUsuarioId() { return usuarioId; }
}
