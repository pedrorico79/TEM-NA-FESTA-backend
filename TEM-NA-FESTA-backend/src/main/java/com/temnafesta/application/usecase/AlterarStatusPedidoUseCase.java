package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.HistoricoStatusPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.HistoricoStatusPedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;

import java.time.LocalDateTime;

public class AlterarStatusPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final HistoricoStatusPedidoRepositoryPort historicoRepositoryPort;

    public AlterarStatusPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                      HistoricoStatusPedidoRepositoryPort historicoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.historicoRepositoryPort = historicoRepositoryPort;
    }

    public Pedido executar(Long pedidoId, StatusProducaoEnum novoStatus, Long usuarioLogadoId, String observacao) {
        Pedido pedido = pedidoRepositoryPort.buscarPorId(pedidoId)
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com ID: " + pedidoId));

        // 1. valida e realiza a transição de status do pedido
        pedido.transitarPara(novoStatus);

        // 2. Salva o pedido atualizado no banco
        Pedido pedidoSalvo = pedidoRepositoryPort.salvar(pedido);

        // 3. Cria e salva o histórico de auditoria
        HistoricoStatusPedido historico = new HistoricoStatusPedido(
                null,
                LocalDateTime.now(),
                observacao,
                novoStatus,
                pedidoSalvo.getId(),
                usuarioLogadoId
        );
        historicoRepositoryPort.salvar(historico);

        return pedidoSalvo;
    }
}