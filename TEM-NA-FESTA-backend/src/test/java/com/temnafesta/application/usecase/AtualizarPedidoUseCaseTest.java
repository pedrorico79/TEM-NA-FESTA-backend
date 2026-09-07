package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarPedidoCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarPedidoUseCaseTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;
    @Mock
    private ProdutoRepositoryPort produtoRepositoryPort;

    @InjectMocks
    private AtualizarPedidoUseCase useCase;

    private static Pedido pedidoRascunho() {
        return new Pedido(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                BigDecimal.ZERO, null, StatusProducaoEnum.RASCUNHO,
                1L, 1L, null, null,
                List.of(new ItemPedido(1L, 1L, 1, new BigDecimal("20.00"), null)), null);
    }

    private static AtualizarPedidoCommand command() {
        return new AtualizarPedidoCommand(1L, LocalDateTime.now().plusDays(5),
                new BigDecimal("5.00"), "Nova obs", null,
                List.of(new AtualizarPedidoCommand.ItemCommand(1L, 2, new BigDecimal("25.00"), null)));
    }

    @Test
    void deveAtualizarDadosSubstituirItensEPersistir() {
        Pedido pedido = pedidoRascunho();
        Produto produto = new Produto(1L, "Bolo", null, new BigDecimal("25.00"), true, false);
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido));
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(produto));
        when(pedidoRepositoryPort.atualizar(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado = useCase.executar(command());

        verify(pedidoRepositoryPort).atualizar(pedido);
        assertTrue(resultado.getValorTotal().compareTo(new BigDecimal("55.00")) == 0);
        assertTrue(resultado.getObservacao().equals("Nova obs"));
    }

    @Test
    void naoDeveAtualizarQuandoPedidoNaoEncontrado() {
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(command()));
        verify(pedidoRepositoryPort, never()).atualizar(any());
    }

    @Test
    void naoDeveAtualizarQuandoProdutoNaoEncontrado() {
        Pedido pedido = pedidoRascunho();
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido));
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(command()));
        verify(pedidoRepositoryPort, never()).atualizar(any());
    }
}
