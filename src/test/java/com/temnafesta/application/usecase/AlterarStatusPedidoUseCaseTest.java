package com.temnafesta.application.usecase;

import com.temnafesta.application.exception.EventoPublicacaoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.HistoricoStatusPedido;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.out.PedidoEventPublisherPort;
import com.temnafesta.domain.ports.out.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.out.repository.HistoricoStatusPedidoRepositoryPort;
import com.temnafesta.domain.ports.out.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * A transição de status é validada no repositório (atualizarStatus, que chama
 * Pedido.transitarPara); o use case registra o histórico e, se pedido,
 * notifica o cliente.
 */
@ExtendWith(MockitoExtension.class)
class AlterarStatusPedidoUseCaseTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;
    @Mock
    private HistoricoStatusPedidoRepositoryPort historicoRepositoryPort;
    @Mock
    private ClienteRepositoryPort clienteRepositoryPort;
    @Mock
    private PedidoEventPublisherPort pedidoEventPublisherPort;

    @InjectMocks
    private AlterarStatusPedidoUseCase useCase;

    private static Pedido pedidoCom(StatusProducaoEnum status) {
        return new Pedido(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                BigDecimal.ZERO, null, status,
                1L, 1L, null, null,
                List.of(new ItemPedido(1L, 1L, 1, new BigDecimal("20.00"), null)), null,
                true, false);
    }

    @Test
    void deveAtualizarStatusERegistrarHistorico() {
        when(pedidoRepositoryPort.atualizarStatus(1L, StatusProducaoEnum.AGUARDANDO_SINAL))
                .thenReturn(pedidoCom(StatusProducaoEnum.AGUARDANDO_SINAL));

        Pedido resultado = useCase.executar(1L, StatusProducaoEnum.AGUARDANDO_SINAL, 9L, "Sinal enviado");

        assertEquals(StatusProducaoEnum.AGUARDANDO_SINAL, resultado.getStatusProducao());
        ArgumentCaptor<HistoricoStatusPedido> captor = ArgumentCaptor.forClass(HistoricoStatusPedido.class);
        verify(historicoRepositoryPort).salvar(captor.capture());
        HistoricoStatusPedido historico = captor.getValue();
        assertEquals(StatusProducaoEnum.AGUARDANDO_SINAL, historico.getStatusProducao());
        assertEquals(9L, historico.getUsuarioId());
        assertEquals("Sinal enviado", historico.getObservacao());
        assertEquals(1L, historico.getPedidoId());
        verify(pedidoEventPublisherPort, never()).publicarNotificacao(any(), any());
    }

    @Test
    void naoDeveRegistrarHistoricoQuandoTransicaoInvalida() {
        when(pedidoRepositoryPort.atualizarStatus(1L, StatusProducaoEnum.ENTREGUE))
                .thenThrow(new RegraDeNegocioException("transição inválida"));

        assertThrows(RegraDeNegocioException.class,
                () -> useCase.executar(1L, StatusProducaoEnum.ENTREGUE, 9L, null));
        verify(historicoRepositoryPort, never()).salvar(any());
        verify(pedidoEventPublisherPort, never()).publicarNotificacao(any(), any());
    }

    @Test
    void deveNotificarClienteQuandoSolicitado() {
        Pedido pedido = pedidoCom(StatusProducaoEnum.CONFIRMADO);
        Cliente cliente = mock(Cliente.class);
        when(pedidoRepositoryPort.atualizarStatus(1L, StatusProducaoEnum.CONFIRMADO)).thenReturn(pedido);
        when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        Pedido resultado = useCase.executar(1L, StatusProducaoEnum.CONFIRMADO, 9L, null);

        assertSame(pedido, resultado);
        assertEquals(true, useCase.notificarCliente(resultado));
        verify(pedidoEventPublisherPort).publicarNotificacao(pedido, cliente);
    }

    @Test
    void falhaAoPublicarNotificacaoNaoDesfazAAlteracao() {
        Pedido pedido = pedidoCom(StatusProducaoEnum.CONFIRMADO);
        Cliente cliente = mock(Cliente.class);
        when(pedidoRepositoryPort.atualizarStatus(1L, StatusProducaoEnum.CONFIRMADO)).thenReturn(pedido);
        when(clienteRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        doThrow(new EventoPublicacaoException("fila fora do ar"))
                .when(pedidoEventPublisherPort).publicarNotificacao(pedido, cliente);

        Pedido resultado = useCase.executar(1L, StatusProducaoEnum.CONFIRMADO, 9L, null);

        assertSame(pedido, resultado);
        assertEquals(false, useCase.notificarCliente(resultado));
        verify(historicoRepositoryPort).salvar(any());
    }
}
