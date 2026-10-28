package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.HistoricoStatusPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.out.PedidoEventPublisherPort;
import com.temnafesta.domain.ports.out.repository.HistoricoStatusPedidoRepositoryPort;
import com.temnafesta.domain.ports.out.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.out.repository.ClienteRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class AlterarStatusPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final HistoricoStatusPedidoRepositoryPort historicoRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final PedidoEventPublisherPort pedidoEventPublisherPort;
    private final Clock relogioNegocio;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AlterarStatusPedidoUseCase.class);

    public AlterarStatusPedidoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort,
            HistoricoStatusPedidoRepositoryPort historicoRepositoryPort,
            ClienteRepositoryPort clienteRepositoryPort,
            PedidoEventPublisherPort pedidoEventPublisherPort) {

        this(
                pedidoRepositoryPort,
                historicoRepositoryPort,
                clienteRepositoryPort,
                pedidoEventPublisherPort,
                com.temnafesta.domain.time.RelogioNegocio.sistema()
        );
    }

    public AlterarStatusPedidoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort,
            HistoricoStatusPedidoRepositoryPort historicoRepositoryPort,
            ClienteRepositoryPort clienteRepositoryPort,
            PedidoEventPublisherPort pedidoEventPublisherPort,
            Clock relogioNegocio) {

        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.historicoRepositoryPort = historicoRepositoryPort;
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.pedidoEventPublisherPort = pedidoEventPublisherPort;
        this.relogioNegocio = relogioNegocio != null
                ? relogioNegocio
                : com.temnafesta.domain.time.RelogioNegocio.sistema();
    }

    @Transactional
    public Pedido executar(Long pedidoId, StatusProducaoEnum novoStatus, Long usuarioLogadoId, String observacao) {

        Pedido pedidoSalvo =
                pedidoRepositoryPort.atualizarStatus(pedidoId, novoStatus);

        HistoricoStatusPedido historico = new HistoricoStatusPedido(
                null,
                LocalDateTime.now(relogioNegocio),
                observacao,
                novoStatus,
                pedidoSalvo.getId(),
                usuarioLogadoId
        );

        historicoRepositoryPort.salvar(historico);

        return pedidoSalvo;
    }

    /**
     * Tenta publicar a notificação depois que a transação de status já foi concluída.
     * Uma falha ao consultar o cliente ou ao publicar no RabbitMQ não desfaz o status.
     */
    public boolean notificarCliente(Pedido pedido) {
        if (pedido == null || pedido.getStatusProducao() == null
                || !pedido.getStatusProducao().deveNotificarCliente()) {
            return true;
        }

        try {
            Cliente cliente = clienteRepositoryPort.buscarPorId(pedido.getClienteId())
                    .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado"));
            pedidoEventPublisherPort.publicarNotificacao(pedido, cliente);
            return true;
        } catch (RuntimeException e) {
            LOGGER.warn("Status do pedido {} foi atualizado, mas não foi possível notificar o cliente.", pedido.getId(), e);
            return false;
        }
    }
}
