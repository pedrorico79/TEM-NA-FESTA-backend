package com.temnafesta.dto.pedido;

import com.temnafesta.model.StatusProducao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Dados para criação ou atualização de pedido")
public class PedidoRequestDto {

    @Schema(description = "Data e hora de entrega do pedido", example = "2026-12-31T18:00:00")
    @NotNull
    @FutureOrPresent
    private LocalDateTime dataEntrega;

//    @Schema(description = "Valor total do pedido (calculado automaticamente na criação)", example = "150.00")
//    private BigDecimal valorTotal;

    @Schema(description = "Observação do pedido", example = "Sem glúten")
    private String observacao;

    @Schema(description = "ID do cliente", example = "1")
    @NotNull
    private Integer clienteId;

    @Schema(description = "ID do usuário responsável", example = "1")
    @NotNull
    private Integer usuarioId;

    @Schema(description = "ID do Status de produção", example = "1")
    @NotNull
    private Integer statusProducaoId;

    @Schema(description = "ID da campanha vinculada ao pedido", example = "1")
    @NotNull
    private Integer campanhaId;

    @Schema(description = "Lista de produtos (itens) do pedido")
    @Valid
    @NotEmpty(message = "O pedido deve conter pelo menos um produto")
    private List<ItemPedidoDto> produtos;

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

//    public BigDecimal getValorTotal() {
//        return valorTotal;
//    }
//
//    public void setValorTotal(BigDecimal valorTotal) {
//        this.valorTotal = valorTotal;
//    }

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

    public Integer getCampanhaId() {
        return campanhaId;
    }

    public void setCampanhaId(Integer campanhaId) {
        this.campanhaId = campanhaId;
    }

    public List<ItemPedidoDto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemPedidoDto> produtos) {
        this.produtos = produtos;
    }
}