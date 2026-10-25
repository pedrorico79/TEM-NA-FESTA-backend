package com.temnafesta.application.usecase;

import com.temnafesta.application.exception.EventoPublicacaoException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.HistoricoStatusPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.out.PedidoEventPublisherPort;
import com.temnafesta.domain.ports.out.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.out.repository.HistoricoStatusPedidoRepositoryPort;
import com.temnafesta.domain.ports.out.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class AlterarStatusPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final HistoricoStatusPedidoRepositoryPort historicoRepositoryPort;
    private final PedidoEventPublisherPort pedidoEventPublisherPort;

    private static final Logger LOGGER = LoggerFactory.getLogger(CriarPedidoInternoUseCase.class);

    public AlterarStatusPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort, ClienteRepositoryPort clienteRepositoryPort,
                                      HistoricoStatusPedidoRepositoryPort historicoRepositoryPort,
                                      PedidoEventPublisherPort pedidoEventPublisherPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.historicoRepositoryPort = historicoRepositoryPort;
        this.pedidoEventPublisherPort = pedidoEventPublisherPort;
    }

    @Transactional
    public Pedido executar(Long pedidoId, StatusProducaoEnum novoStatus, Long usuarioLogadoId, String observacao, boolean notificarCliente) {
        // Valida a transição no estado atual do pedido e atualiza a entidade gerenciada.
        Pedido pedidoSalvo = pedidoRepositoryPort.atualizarStatus(pedidoId, novoStatus);

        Cliente cliente = clienteRepositoryPort.buscarPorId(pedidoSalvo.getClienteId())
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado"));

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


        if (notificarCliente && pedidoSalvo.getStatusProducao().deveNotificarCliente()) {
            try {
                pedidoEventPublisherPort.publicarNotificacao(pedidoSalvo, cliente);
            } catch (EventoPublicacaoException e) {
                LOGGER.error("Nao foi possivel publicar o evento.", e);
            }
        }

        return pedidoSalvo;
    }
}
