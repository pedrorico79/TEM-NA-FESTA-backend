package com.temnafesta.domain.model;

import com.temnafesta.domain.vo.StatusPagamentoEnum;
import com.temnafesta.domain.vo.TipoPagamentoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {
    private Long id;
    private BigDecimal valor;
    private LocalDateTime dataPagamento;
    private TipoPagamentoEnum tipoPagamento;
    private StatusPagamentoEnum statusPagamento;
    private Long metodoPagamentoId;
    private Long usuarioId;

    public Pagamento(Long id, BigDecimal valor, LocalDateTime dataPagamento,
                     TipoPagamentoEnum tipoPagamento, StatusPagamentoEnum statusPagamento,
                     Long metodoPagamentoId, Long usuarioId) {
        this.id = id;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.tipoPagamento = tipoPagamento;
        this.statusPagamento = statusPagamento;
        this.metodoPagamentoId = metodoPagamentoId;
        this.usuarioId = usuarioId;
    }

    public boolean isConfirmado() {
        return StatusPagamentoEnum.CONFIRMADO.equals(this.statusPagamento);
    }

    // Getters
    public Long getId() { return id; }
    public BigDecimal getValor() { return valor; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public TipoPagamentoEnum getTipoPagamento() { return tipoPagamento; }
    public StatusPagamentoEnum getStatusPagamento() { return statusPagamento; }
    public Long getMetodoPagamentoId() { return metodoPagamentoId; }
    public Long getUsuarioId() { return usuarioId; }
}