package com.temnafesta.application.usecase;

import com.temnafesta.application.exception.EventoPublicacaoException;
import com.temnafesta.domain.exception.NaoEncontradoException;
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
    public Pedido executar(Long pedidoId, StatusProducaoEnum novoStatus, Long usuarioLogadoId, String observacao, boolean notificarCliente) {

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

        if (notificarCliente && pedidoSalvo.getStatusProducao().deveNotificarCliente()) {

            Cliente cliente = clienteRepositoryPort.buscarPorId(pedidoSalvo.getClienteId())
                    .orElseThrow(() -> new NaoEncontradoException("Cliente não encontrado"));

            try {
                pedidoEventPublisherPort.publicarNotificacao(pedidoSalvo, cliente);
            } catch (EventoPublicacaoException e) {
                LOGGER.error("Nao foi possivel publicar o evento.", e);
            }
        }

        return pedidoSalvo;
    }
}