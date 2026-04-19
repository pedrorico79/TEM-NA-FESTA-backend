package com.temnafesta.dto.pedido;

import com.temnafesta.model.StatusProducao;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PedidoRequestDto {

    @NotNull
    @FutureOrPresent
    private LocalDateTime dataEntrega;

    @NotNull
    @Positive
    private BigDecimal valorTotal;

    private String observacao;

    @NotNull
    private Integer clienteId;

    @NotNull
    private Integer usuarioId;

    @NotNull
    private StatusProducao statusProducao;

    @NotNull
    private Integer campanhaId;

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public StatusProducao getStatusProducao() {
        return statusProducao;
    }

    public void setStatusProducao(StatusProducao statusProducao) {
        this.statusProducao = statusProducao;
    }

    public Integer getCampanhaId() {
        return campanhaId;
    }

    public void setCampanhaId(Integer campanhaId) {
        this.campanhaId = campanhaId;
    }
}