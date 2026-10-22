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
    private String chaveIdempotencia;
    private String assinaturaRequisicao;

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

    public boolean isPendente() {
        return StatusPagamentoEnum.PENDENTE.equals(this.statusPagamento);
    }

    public void confirmar() {
        if (!isPendente()) {
            throw new com.temnafesta.domain.exception.RegraDeNegocioException(
                    "Somente pagamentos pendentes podem ser confirmados.");
        }
        this.statusPagamento = StatusPagamentoEnum.CONFIRMADO;
    }

    public void cancelar() {
        if (StatusPagamentoEnum.CANCELADO.equals(this.statusPagamento)) {
            throw new com.temnafesta.domain.exception.RegraDeNegocioException(
                    "Este pagamento já está cancelado.");
        }
        this.statusPagamento = StatusPagamentoEnum.CANCELADO;
    }

    // Getters
    public Long getId() { return id; }
    public BigDecimal getValor() { return valor; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public TipoPagamentoEnum getTipoPagamento() { return tipoPagamento; }
    public StatusPagamentoEnum getStatusPagamento() { return statusPagamento; }
    public Long getMetodoPagamentoId() { return metodoPagamentoId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getChaveIdempotencia() { return chaveIdempotencia; }
    public void setChaveIdempotencia(String chaveIdempotencia) { this.chaveIdempotencia = chaveIdempotencia; }
    public String getAssinaturaRequisicao() { return assinaturaRequisicao; }
    public void setAssinaturaRequisicao(String assinaturaRequisicao) { this.assinaturaRequisicao = assinaturaRequisicao; }
}
