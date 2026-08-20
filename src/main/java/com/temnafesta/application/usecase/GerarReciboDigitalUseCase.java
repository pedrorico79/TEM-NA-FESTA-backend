package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class GerarReciboDigitalUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;

    public GerarReciboDigitalUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                     ClienteRepositoryPort clienteRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    public String executar(Long pedidoId) {
        Pedido pedido = pedidoRepositoryPort.buscarPorId(pedidoId)
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com ID: " + pedidoId));

        Cliente cliente = clienteRepositoryPort.buscarPorId(pedido.getClienteId())
                .orElseThrow(() -> new RegraDeNegocioException("Cliente não encontrado."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        BigDecimal valorSinal = pedido.getValorTotal().multiply(new BigDecimal("0.50"));

        StringBuilder recibo = new StringBuilder();
        recibo.append("🎂 *TEM NA FESTA - CONFIRMAÇÃO DE PEDIDO #").append(pedido.getId()).append("*\n\n");
        recibo.append("*Cliente:* ").append(cliente.getNome()).append("\n");
        recibo.append("*Data de Entrega:* ").append(pedido.getDataEntrega().format(formatter)).append("\n\n");
        recibo.append("*Itens do Pedido:*\n");

        for (ItemPedido item : pedido.getItens()) {
            recibo.append("- ").append(item.getQuantidade()).append("x Item (R$ ")
                    .append(item.getPrecoUnitario()).append(")");
            if (item.getObservacaoItem() != null && !item.getObservacaoItem().isBlank()) {
                recibo.append(" | ").append(item.getObservacaoItem());
            }
            recibo.append("\n");
        }

        if (pedido.getTaxaEntrega().compareTo(BigDecimal.ZERO) > 0) {
            recibo.append("\n*Taxa de Entrega:* R$ ").append(pedido.getTaxaEntrega());
        }

        recibo.append("\n\n*Valor Total:* R$ ").append(pedido.getValorTotal());
        recibo.append("\n*Sinal Mínimo (50%):* R$ ").append(valorSinal);
        recibo.append("\n\n*Chave PIX:* pix@temnafesta.com.br");
        recibo.append("\n\n_Por favor, nos envie o comprovante para confirmar a produção!_");

        return recibo.toString();
    }
}