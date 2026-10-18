package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarPedidoCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.domain.vo.StatusPagamentoEnum;
import com.temnafesta.domain.vo.TipoPagamentoEnum;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
                List.of(new ItemPedido(1L, 1L, 1, new BigDecimal("20.00"), null)), null,
                true, false);
    }

    private static AtualizarPedidoCommand command() {
        return new AtualizarPedidoCommand(1L, LocalDateTime.now().plusDays(5),
                new BigDecimal("5.00"), "Nova obs", null, null,
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
        assertEquals(produto, resultado.getItens().getFirst().getProduto());
    }

    @Test
    void deveRemoverEventoQuandoAtualizacaoRecebeEventoNulo() {
        Pedido pedido = new Pedido(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                BigDecimal.ZERO, null, StatusProducaoEnum.RASCUNHO, 1L, 1L, 99L, null,
                List.of(new ItemPedido(1L, 1L, 1, new BigDecimal("20.00"), null)), null,
                true, false);
        configurarPedidoComProduto(pedido, new Produto(1L, "Bolo", null, new BigDecimal("25.00"), true, false));
        stubAtualizacaoPedido();

        Pedido resultado = useCase.executar(command());

        assertNull(resultado.getEventoId());
        verify(pedidoRepositoryPort).atualizar(pedido);
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

    @Test
    void devePermitirManterProdutoInativoQueJaPertenceAoPedido() {
        Pedido pedido = pedidoRascunho();
        Produto produtoInativo = new Produto(1L, "Bolo", null, new BigDecimal("25.00"), false, false);
        configurarPedidoComProduto(pedido, produtoInativo);
        stubAtualizacaoPedido();

        useCase.executar(command());

        verify(pedidoRepositoryPort).atualizar(pedido);
    }

    @Test
    void naoDeveAdicionarProdutoInativoNovoAoPedido() {
        Pedido pedido = pedidoRascunho();
        Produto produtoInativo = new Produto(2L, "Bolo antigo", null, new BigDecimal("25.00"), false, false);
        AtualizarPedidoCommand comando = new AtualizarPedidoCommand(1L, LocalDateTime.now().plusDays(5),
                new BigDecimal("5.00"), "Nova obs", null, null,
                List.of(new AtualizarPedidoCommand.ItemCommand(2L, 2, new BigDecimal("25.00"), null)));
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido));
        when(produtoRepositoryPort.buscarPorId(2L)).thenReturn(Optional.of(produtoInativo));

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(comando));
        verify(pedidoRepositoryPort, never()).atualizar(any());
    }

    @Test
    void deveManterProdutoExcluidoLogicamenteQueJaPertenceAoPedido() {
        Pedido pedido = pedidoRascunho();
        Produto produtoExcluido = new Produto(1L, "Bolo", null, new BigDecimal("25.00"), false, true);
        configurarPedidoComProduto(pedido, produtoExcluido);
        when(produtoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.empty());
        stubAtualizacaoPedido();

        useCase.executar(command());

        verify(pedidoRepositoryPort).atualizar(pedido);
    }

    @Test
    void naoDeveReduzirTotalAbaixoDosPagamentosAtivos() {
        Pedido pedido = pedidoComPagamentos(List.of(
                pagamento("30.00", StatusPagamentoEnum.CONFIRMADO),
                pagamento("30.00", StatusPagamentoEnum.PENDENTE)));
        configurarPedidoComProduto(pedido, new Produto(1L, "Bolo", null, new BigDecimal("25.00"), true, false));

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(command()));
        verify(pedidoRepositoryPort, never()).atualizar(any());
    }

    @Test
    void pagamentosCanceladosNaoBloqueiamReducaoDoTotal() {
        Pedido pedido = pedidoComPagamentos(List.of(pagamento("100.00", StatusPagamentoEnum.CANCELADO)));
        configurarPedidoComProduto(pedido, new Produto(1L, "Bolo", null, new BigDecimal("25.00"), true, false));
        when(pedidoRepositoryPort.atualizar(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.executar(command());

        verify(pedidoRepositoryPort).atualizar(pedido);
    }

    private void configurarPedidoComProduto(Pedido pedido, Produto produto) {
        pedido.getItens().getFirst().setProduto(produto);
        when(pedidoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(pedido));
        when(produtoRepositoryPort.buscarPorId(produto.getId()))
                .thenReturn(produto.isDeletado() ? Optional.empty() : Optional.of(produto));
    }

    private void stubAtualizacaoPedido() {
        when(pedidoRepositoryPort.atualizar(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private Pedido pedidoComPagamentos(List<Pagamento> pagamentos) {
        return new Pedido(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                BigDecimal.ZERO, null, StatusProducaoEnum.RASCUNHO, 1L, 1L, null, null,
                List.of(new ItemPedido(1L, 1L, 1, new BigDecimal("20.00"), null)), pagamentos,
                true, false);
    }

    private static Pagamento pagamento(String valor, StatusPagamentoEnum status) {
        return new Pagamento(null, new BigDecimal(valor), LocalDateTime.now(), TipoPagamentoEnum.SINAL,
                status, 1L, 1L);
    }
}
