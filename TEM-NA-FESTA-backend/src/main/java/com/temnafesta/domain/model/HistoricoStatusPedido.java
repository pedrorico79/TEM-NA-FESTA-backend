package com.temnafesta.domain.model;

import com.temnafesta.domain.vo.StatusProducaoEnum;
import java.time.LocalDateTime;

public class HistoricoStatusPedido {
    private Long id;
    private LocalDateTime dataAlteracao;
    private String observacao;
    private StatusProducaoEnum statusProducao;
    private Long pedidoId;
    private Long usuarioId;

    public HistoricoStatusPedido(Long id, LocalDateTime dataAlteracao, String observacao,
                                 StatusProducaoEnum statusProducao, Long pedidoId, Long usuarioId) {
        this.id = id;
        this.dataAlteracao = dataAlteracao != null ? dataAlteracao : LocalDateTime.now();
        this.observacao = observacao;
        this.statusProducao = statusProducao;
        this.pedidoId = pedidoId;
        this.usuarioId = usuarioId;
    }

    // Getters
    public Long getId() { return id; }
    public LocalDateTime getDataAlteracao() { return dataAlteracao; }
    public String getObservacao() { return observacao; }
    public StatusProducaoEnum getStatusProducao() { return statusProducao; }
    public Long getPedidoId() { return pedidoId; }
    public Long getUsuarioId() { return usuarioId; }
}