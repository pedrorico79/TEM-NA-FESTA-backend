package com.temnafesta.dto.pedido;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class PedidoRequestDto {

    @NotNull
    @FutureOrPresent
    private LocalDate dataEntrega;

    @NotNull
    @Positive
    private Double valorTotal;

    private String observacao;

    @NotNull
    private Integer clienteId;

    @NotNull
    private Integer usuarioId;

    @NotNull
    private Integer statusProducaoId;

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
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

    public Integer getStatusProducaoId() {
        return statusProducaoId;
    }

    public void setStatusProducaoId(Integer statusProducaoId) {
        this.statusProducaoId = statusProducaoId;
    }
}