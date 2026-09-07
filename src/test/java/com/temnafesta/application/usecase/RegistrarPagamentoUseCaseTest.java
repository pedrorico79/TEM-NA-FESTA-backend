package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusPagamentoEnum;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.domain.vo.TipoPagamentoEnum;
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
class RegistrarPagamentoUseCaseTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;

    @InjectMocks
    private RegistrarPagamentoUseCase useCase;

    private static Pedido pedido(StatusProducaoEnum status) {
        return new Pedido(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                BigDecimal.ZERO, null, status,
                1L, 1L, null, null,
                List.of(new ItemPedido(1L, 1L, 2, new BigDecimal("50.00"), null)), null);
    }

    @Test
    void deveRegistrarPagamentoConfirmadoESalvar() {
        Pedido pedido = pedido(StatusProducaoEnum.RASCUNHO);
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepositoryPort.salvar(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado = useCase.executar(1L, new BigDecimal("30.00"),
                TipoPagamentoEnum.SINAL, 5L, 9L);

        assertEquals(1, resultado.getPagamentos().size());
        assertEquals(StatusPagamentoEnum.CONFIRMADO, resultado.getPagamentos().get(0).getStatusPagamento());
        assertEquals(StatusProducaoEnum.RASCUNHO, resultado.getStatusProducao());
        verify(pedidoRepositoryPort).salvar(pedido);
    }

    @Test
    void deveConfirmarAutomaticamenteQuandoSinalAtingidoEmAguardandoSinal() {
        Pedido pedido = pedido(StatusProducaoEnum.AGUARDANDO_SINAL);
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepositoryPort.salvar(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado = useCase.executar(1L, new BigDecimal("50.00"),
                TipoPagamentoEnum.SINAL, 5L, 9L);

        assertEquals(StatusProducaoEnum.CONFIRMADO, resultado.getStatusProducao());
    }

    @Test
    void naoDeveRegistrarQuandoPedidoNaoEncontrado() {
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(
                1L, new BigDecimal("10.00"), TipoPagamentoEnum.SINAL, 5L, 9L));
        verify(pedidoRepositoryPort, never()).salvar(any());
    }

    @Test
    void devePersistirMetodoEUsuarioDoPagamento() {
        Pedido pedido = pedido(StatusProducaoEnum.RASCUNHO);
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepositoryPort.salvar(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.executar(1L, new BigDecimal("10.00"), TipoPagamentoEnum.QUITACAO, 5L, 9L);

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepositoryPort).salvar(captor.capture());
        assertEquals(5L, captor.getValue().getPagamentos().get(0).getMetodoPagamentoId());
        assertEquals(9L, captor.getValue().getPagamentos().get(0).getUsuarioId());
        assertEquals(TipoPagamentoEnum.QUITACAO, captor.getValue().getPagamentos().get(0).getTipoPagamento());
    }
}
