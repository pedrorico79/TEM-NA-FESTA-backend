package com.temnafesta.dto.pagamento;

import com.temnafesta.model.MetodoPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados para criação de pagamento")
public class PagamentoRequestDto {

    @Schema(description = "Valor do pagamento", example = "150.00")
    @NotNull
    @Positive
    private BigDecimal valor;

    @Schema(description = "Método de pagamento", example = "PIX")
    @NotNull
    private MetodoPagamento metodo;

    @Schema(description = "ID do pedido", example = "1")
    @NotNull
    private Integer pedidoId;

    @Schema(description = "ID do usuário", example = "1")
    @NotNull
    private Integer usuarioId;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPagamento metodo) {
        this.metodo = metodo;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}