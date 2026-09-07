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
class ListarHistoricoStatusPedidoUseCaseTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;

    @Mock
    private HistoricoStatusPedidoRepositoryPort historicoRepositoryPort;

    @InjectMocks
    private ListarHistoricoStatusPedidoUseCase useCase;

    private static Pedido pedido() {
        return new Pedido(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                BigDecimal.ZERO, null, StatusProducaoEnum.RASCUNHO,
                1L, 1L, null, null,
                List.of(new ItemPedido(1L, 1L, 2, new BigDecimal("50.00"), null)), null);
    }

    @Test
    void deveListarHistoricoQuandoPedidoExiste() {
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido()));
        List<HistoricoStatusPedido> historico = List.of(
                new HistoricoStatusPedido(1L, LocalDateTime.now(), "obs", StatusProducaoEnum.CONFIRMADO, 1L, 9L));
        when(historicoRepositoryPort.buscarPorPedidoId(1L)).thenReturn(historico);

        List<HistoricoStatusPedido> resultado = useCase.executar(1L);

        assertEquals(1, resultado.size());
        assertEquals(StatusProducaoEnum.CONFIRMADO, resultado.get(0).getStatusProducao());
    }

    @Test
    void naoDeveListarQuandoPedidoNaoEncontrado() {
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(1L));
        verify(historicoRepositoryPort, never()).buscarPorPedidoId(any());
    }
}
