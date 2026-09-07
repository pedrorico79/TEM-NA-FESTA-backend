package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcluirPagamentoUseCaseTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;

    @InjectMocks
    private ExcluirPagamentoUseCase useCase;

    private static Pedido pedidoComPagamento(Long pagamentoId) {
        Pedido pedido = new Pedido(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                BigDecimal.ZERO, null, StatusProducaoEnum.RASCUNHO,
                1L, 1L, null, null,
                List.of(new ItemPedido(1L, 1L, 2, new BigDecimal("50.00"), null)), null);

        pedido.adicionarPagamento(new Pagamento(pagamentoId, new BigDecimal("30.00"), LocalDateTime.now(),
                TipoPagamentoEnum.SINAL, StatusPagamentoEnum.CONFIRMADO, 5L, 9L));

        return pedido;
    }

    @Test
    void deveRemoverPagamentoESalvarPedido() {
        Pedido pedido = pedidoComPagamento(7L);
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepositoryPort.salvar(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.executar(1L, 7L);

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepositoryPort).salvar(captor.capture());
        assertTrue(captor.getValue().getPagamentos().isEmpty());
    }

    @Test
    void naoDeveExcluirQuandoPedidoNaoEncontrado() {
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(1L, 7L));
        verify(pedidoRepositoryPort, never()).salvar(any());
    }

    @Test
    void naoDeveExcluirQuandoPagamentoNaoPertenceAoPedido() {
        Pedido pedido = pedidoComPagamento(7L);
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido));

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(1L, 999L));
        assertEquals(1, pedido.getPagamentos().size());
        verify(pedidoRepositoryPort, never()).salvar(any());
    }
}
