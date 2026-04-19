package com.temnafesta.dto.pedido;

import com.temnafesta.model.StatusProducao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados para criação ou atualização de pedido")
public class PedidoRequestDto {

    @Schema(description = "Data e hora de entrega do pedido", example = "2024-12-31T18:00:00")
    @NotNull
    @FutureOrPresent
    private LocalDateTime dataEntrega;

    @Schema(description = "Valor total do pedido", example = "150.00")
    @NotNull
    @Positive
    private BigDecimal valorTotal;

    @Schema(description = "Observação do pedido", example = "Sem glúten")
    private String observacao;

    @Schema(description = "ID do cliente", example = "1")
    @NotNull
    private Integer clienteId;

    @Schema(description = "ID do usuário responsável", example = "1")
    @NotNull
    private Integer usuarioId;

    @Schema(description = "Status de produção do pedido", example = "EM_PRODUCAO")
    @NotNull
    private StatusProducao statusProducao;

    @Schema(description = "ID da campanha vinculada ao pedido", example = "1")
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