package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.HistoricoStatusPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.HistoricoStatusPedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public class AlterarStatusPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final HistoricoStatusPedidoRepositoryPort historicoRepositoryPort;

    public AlterarStatusPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                      HistoricoStatusPedidoRepositoryPort historicoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.historicoRepositoryPort = historicoRepositoryPort;
    }

    @Transactional
    public Pedido executar(Long pedidoId, StatusProducaoEnum novoStatus, Long usuarioLogadoId, String observacao) {
        // Valida a transição no estado atual do pedido e atualiza a entidade gerenciada.
        Pedido pedidoSalvo = pedidoRepositoryPort.atualizarStatus(pedidoId, novoStatus);

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
