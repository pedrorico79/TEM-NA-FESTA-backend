package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusPagamentoEnum;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.domain.vo.TipoPagamentoEnum;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoLeituraUseCasesTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;

    @InjectMocks
    private ListarPedidoPorIdUseCase listarPedidoPorIdUseCase;
    @InjectMocks
    private ListarPedidosUseCase listarPedidosUseCase;
    @InjectMocks
    private ContarPorStatusUseCase contarPorStatusUseCase;
    @InjectMocks
    private ListarProximasRetiradasUseCase listarProximasRetiradasUseCase;
    @InjectMocks
    private ListarPagamentosPedidoUseCase listarPagamentosPedidoUseCase;
    @InjectMocks
    private ListarItemPedidoPorIdUseCase listarItemPedidoPorIdUseCase;

    private static Pedido pedido() {
        return new Pedido(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(2),
                BigDecimal.ZERO, null, StatusProducaoEnum.RASCUNHO,
                1L, 1L, null, null,
                List.of(new ItemPedido(5L, 1L, 1, new BigDecimal("20.00"), null)), null);
    }

    @Nested
    class PorId {
        @Test
        void deveRetornarPedidoQuandoExiste() {
            when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido()));

            assertEquals(1L, listarPedidoPorIdUseCase.executar(1L).getId());
        }

        @Test
        void naoDeveRetornarQuandoNaoExiste() {
            when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

            assertThrows(RegraDeNegocioException.class, () -> listarPedidoPorIdUseCase.executar(1L));
        }
    }

    @Nested
    class Listar {
        @Test
        void deveTrimarBuscaAntesDeDelegar() {
            when(pedidoRepositoryPort.listarPedidos("bolo", StatusProducaoEnum.RASCUNHO, null))
                    .thenReturn(List.of(pedido()));

            assertEquals(1, listarPedidosUseCase.executar("  bolo  ", StatusProducaoEnum.RASCUNHO, null).size());
        }

        @Test
        void deveConverterBuscaVaziaEmNulo() {
            when(pedidoRepositoryPort.listarPedidos(null, null, 3L)).thenReturn(List.of());

            assertTrue(listarPedidosUseCase.executar("   ", null, 3L).isEmpty());
            verify(pedidoRepositoryPort).listarPedidos(null, null, 3L);
        }

        @Test
        void deveContarTodosOsStatus() {
            for (StatusProducaoEnum status : StatusProducaoEnum.values()) {
                when(pedidoRepositoryPort.contarPorStatus(status)).thenReturn(2L);
            }

            Map<StatusProducaoEnum, Long> contagem = contarPorStatusUseCase.executar();

            assertEquals(7, contagem.size());
            assertTrue(contagem.values().stream().allMatch(v -> v == 2L));
        }
    }

    @Nested
    class Retiradas {
        @Test
        void deveListarComLimiteNoFimDoDia() {
            when(pedidoRepositoryPort.listarProximasRetiradas(any(LocalDateTime.class)))
                    .thenReturn(List.of(pedido()));

            assertEquals(1, listarProximasRetiradasUseCase.executar(3).size());

            ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
            verify(pedidoRepositoryPort).listarProximasRetiradas(captor.capture());
            LocalDateTime limite = captor.getValue();
            assertEquals(23, limite.getHour());
            assertEquals(59, limite.getMinute());
            assertEquals(LocalDateTime.now().plusDays(3).toLocalDate(), limite.toLocalDate());
        }

        @Test
        void naoDeveAceitarDiasNegativos() {
            assertThrows(IllegalArgumentException.class, () -> listarProximasRetiradasUseCase.executar(-1));
        }
    }

    @Nested
    class PagamentosEItens {
        @Test
        void deveListarPagamentosDelegandoAoPort() {
            Pagamento pagamento = new Pagamento(1L, new BigDecimal("10.00"), LocalDateTime.now(),
                    TipoPagamentoEnum.SINAL, StatusPagamentoEnum.CONFIRMADO, 1L, 1L);
            when(pedidoRepositoryPort.listarPagamentos(1L)).thenReturn(List.of(pagamento));

            assertEquals(1, listarPagamentosPedidoUseCase.executar(1L).size());
        }

        @Test
        void deveBuscarItemQuandoExiste() {
            ItemPedido item = new ItemPedido(5L, 1L, 1, new BigDecimal("20.00"), null);
            when(pedidoRepositoryPort.buscarItemPorId(1L, 5L)).thenReturn(Optional.of(item));

            assertEquals(5L, listarItemPedidoPorIdUseCase.executar(1L, 5L).getId());
        }

        @Test
        void naoDeveBuscarItemInexistente() {
            when(pedidoRepositoryPort.buscarItemPorId(1L, 9L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> listarItemPedidoPorIdUseCase.executar(1L, 9L));
        }
    }

    @Test
    void deveListarPedidosComFiltrosNulos() {
        when(pedidoRepositoryPort.listarPedidos(isNull(), isNull(), isNull()))
                .thenReturn(List.of(pedido(), pedido()));

        assertEquals(2, listarPedidosUseCase.executar(null, null, null).size());
        verify(pedidoRepositoryPort).listarPedidos(eq(null), eq(null), eq(null));
    }
}
