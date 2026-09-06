package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.HistoricoStatusPedido;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.HistoricoStatusPedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlterarStatusPedidoUseCaseTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;
    @Mock
    private HistoricoStatusPedidoRepositoryPort historicoRepositoryPort;

    @InjectMocks
    private AlterarStatusPedidoUseCase useCase;

    private static Pedido pedidoRascunho() {
        return new Pedido(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                BigDecimal.ZERO, null, StatusProducaoEnum.RASCUNHO,
                1L, 1L, null, null,
                List.of(new ItemPedido(1L, 1L, 1, new BigDecimal("20.00"), null)), null);
    }

    @Test
    void deveTransitarSalvarERegistrarHistorico() {
        Pedido pedido = pedidoRascunho();
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepositoryPort.salvar(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado = useCase.executar(1L, StatusProducaoEnum.AGUARDANDO_SINAL, 9L, "Sinal enviado");

        assertEquals(StatusProducaoEnum.AGUARDANDO_SINAL, resultado.getStatusProducao());
        verify(pedidoRepositoryPort).salvar(pedido);
        ArgumentCaptor<HistoricoStatusPedido> captor = ArgumentCaptor.forClass(HistoricoStatusPedido.class);
        verify(historicoRepositoryPort).salvar(captor.capture());
        HistoricoStatusPedido historico = captor.getValue();
        assertEquals(StatusProducaoEnum.AGUARDANDO_SINAL, historico.getStatusProducao());
        assertEquals(9L, historico.getUsuarioId());
        assertEquals("Sinal enviado", historico.getObservacao());
        assertEquals(1L, historico.getPedidoId());
    }

    @Test
    void naoDeveAlterarQuandoPedidoNaoEncontrado() {
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class,
                () -> useCase.executar(1L, StatusProducaoEnum.CANCELADO, 9L, null));
        verify(pedidoRepositoryPort, never()).salvar(any());
        verify(historicoRepositoryPort, never()).salvar(any());
    }

    @Test
    void naoDeveSalvarNemHistoriarTransicaoInvalida() {
        Pedido pedido = pedidoRascunho();
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido));

        assertThrows(RegraDeNegocioException.class,
                () -> useCase.executar(1L, StatusProducaoEnum.ENTREGUE, 9L, null));
        verify(pedidoRepositoryPort, never()).salvar(any());
        verify(historicoRepositoryPort, never()).salvar(any());
    }
}
