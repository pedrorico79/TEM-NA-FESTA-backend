package com.temnafesta.dto.historicosStatusPedido;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class HistoricoStatusPedidoResponseDto {

    @Schema(description = "ID do histórico status pedido", example = "1")
    private Integer id;

    @Schema(description = "ID do pedido", example = "1")
    private Integer pedidoId;

    @Schema(description = "ID do status", example = "1")
    private Integer statusId;

    @Schema(description = "Nome do status", example = "ENTREGUE")
    private String statusNome;

    @Schema(description = "ID do usuário", example = "1")
    private Integer usuarioId;

    @Schema(description = "Nome do usuário", example = "Maria")
    private String usuarioNome;

    @Schema(description = "Data da alteração", example = "2026-05-20")
    private LocalDateTime dataAlteracao;

    @Schema(description = "Observação", example = "Pedido adiado por pedido da cliente")
    private String observacao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusNome() {
        return statusNome;
    }

    public void setStatusNome(String statusNome) {
        this.statusNome = statusNome;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}