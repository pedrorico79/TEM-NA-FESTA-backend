package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusPagamentoEnum;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.domain.vo.TipoPagamentoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegistrarPagamentoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public RegistrarPagamentoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public Pedido executar(Long pedidoId, BigDecimal valor, TipoPagamentoEnum tipo, Long metodoPagamentoId, Long usuarioId) {
        Pedido pedido = pedidoRepositoryPort.buscarPorId(pedidoId)
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com ID: " + pedidoId));

        Pagamento pagamento = new Pagamento(
                null,
                valor,
                LocalDateTime.now(),
                tipo,
                StatusPagamentoEnum.CONFIRMADO,
                metodoPagamentoId,
                usuarioId
        );

        pedido.adicionarPagamento(pagamento);

        // Se o pedido estava em RASCUNHO/AGUARDANDO_SINAL e o sinal foi atingido, confirma automaticamente
        if (StatusProducaoEnum.AGUARDANDO_SINAL.equals(pedido.getStatusProducao()) && pedido.isSinalPago()) {
            pedido.transitarPara(StatusProducaoEnum.CONFIRMADO);
        }

        return pedidoRepositoryPort.salvar(pedido);
    }
}