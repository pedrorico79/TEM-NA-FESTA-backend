package com.temnafesta.domain.model;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.vo.StatusPagamentoEnum;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.domain.vo.TipoPagamentoEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PedidoTest {

    private static ItemPedido item(BigDecimal preco, int quantidade) {
        return new ItemPedido(null, 1L, quantidade, preco, null);
    }

    private static Pagamento pagamentoConfirmado(String valor) {
        return new Pagamento(null, new BigDecimal(valor), LocalDateTime.now(),
                TipoPagamentoEnum.SINAL, StatusPagamentoEnum.CONFIRMADO, 1L, 1L);
    }

    private static Pagamento pagamentoPendente(String valor) {
        return new Pagamento(null, new BigDecimal(valor), LocalDateTime.now(),
                TipoPagamentoEnum.SINAL, StatusPagamentoEnum.PENDENTE, 1L, 1L);
    }

    /** 2 x 50.00 + taxa 10.00 = 110.00; sinal mínimo (50%) = 55.00. */
    private static Pedido pedidoRascunho() {
        return new Pedido(null, null, LocalDateTime.now().plusDays(7),
                new BigDecimal("10.00"), null, null,
                1L, 1L, null, null,
                List.of(item(new BigDecimal("50.00"), 2)), null);
    }

    @Test
    void deveCriarPedidoComDefaultsERecalcularTotal() {
        Pedido pedido = pedidoRascunho();

        assertEquals(StatusProducaoEnum.RASCUNHO, pedido.getStatusProducao());
        assertNotNull(pedido.getDataPedido());
        assertTrue(pedido.getValorTotal().compareTo(new BigDecimal("110.00")) == 0);
        assertFalse(pedido.isDeletado());
        assertTrue(pedido.isAtivo());
    }

    @Test
    void deveAssumirTaxaZeroQuandoNula() {
        Pedido pedido = new Pedido(null, null, LocalDateTime.now().plusDays(7),
                null, null, null, 1L, 1L, null, null,
                List.of(item(new BigDecimal("50.00"), 2)), null);

        assertTrue(pedido.getTaxaEntrega().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(pedido.getValorTotal().compareTo(new BigDecimal("100.00")) == 0);
    }

    @Test
    void devePercorrerCadeiaFelizAteEntrega() {
        Pedido pedido = pedidoRascunho();
        pedido.adicionarPagamento(pagamentoConfirmado("55.00"));

        pedido.transitarPara(StatusProducaoEnum.AGUARDANDO_SINAL);
        pedido.transitarPara(StatusProducaoEnum.CONFIRMADO);
        pedido.transitarPara(StatusProducaoEnum.EM_PRODUCAO);
        pedido.transitarPara(StatusProducaoEnum.PRONTO_PARA_ENTREGA);
        pedido.transitarPara(StatusProducaoEnum.ENTREGUE);

        assertEquals(StatusProducaoEnum.ENTREGUE, pedido.getStatusProducao());
    }

    @Test
    void naoDevePermitirTransicaoDiretaInvalida() {
        Pedido pedido = pedidoRascunho();

        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class,
                () -> pedido.transitarPara(StatusProducaoEnum.EM_PRODUCAO));

        assertTrue(ex.getMessage().contains("Não é permitida a transição direta"));
        assertEquals(StatusProducaoEnum.RASCUNHO, pedido.getStatusProducao());
    }

    @Test
    void naoDeveIniciarProducaoSemSinalPago() {
        Pedido pedido = pedidoRascunho();
        pedido.transitarPara(StatusProducaoEnum.AGUARDANDO_SINAL);
        pedido.transitarPara(StatusProducaoEnum.CONFIRMADO);

        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class,
                () -> pedido.transitarPara(StatusProducaoEnum.EM_PRODUCAO));

        assertTrue(ex.getMessage().contains("sinal"));
    }

    @Test
    void deveIniciarProducaoComSinalExatoDe50Porcento() {
        Pedido pedido = pedidoRascunho();
        pedido.adicionarPagamento(pagamentoConfirmado("55.00"));

        assertTrue(pedido.isSinalPago());

        pedido.transitarPara(StatusProducaoEnum.AGUARDANDO_SINAL);
        pedido.transitarPara(StatusProducaoEnum.CONFIRMADO);
        pedido.transitarPara(StatusProducaoEnum.EM_PRODUCAO);

        assertEquals(StatusProducaoEnum.EM_PRODUCAO, pedido.getStatusProducao());
    }

    @Test
    void naoDeveConsiderarPagamentoPendenteNoSinal() {
        Pedido pedido = pedidoRascunho();
        pedido.adicionarPagamento(pagamentoPendente("100.00"));

        assertFalse(pedido.isSinalPago());
        assertTrue(pedido.calcularTotalPago().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    void deveSomarSomentePagamentosConfirmados() {
        Pedido pedido = pedidoRascunho();
        pedido.adicionarPagamento(pagamentoConfirmado("30.00"));
        pedido.adicionarPagamento(pagamentoPendente("100.00"));

        assertTrue(pedido.calcularTotalPago().compareTo(new BigDecimal("30.00")) == 0);
    }

    @ParameterizedTest
    @EnumSource(value = StatusProducaoEnum.class,
            names = {"EM_PRODUCAO", "PRONTO_PARA_ENTREGA", "ENTREGUE", "CANCELADO"})
    void naoDeveAdicionarItemAposInicioDaProducao(StatusProducaoEnum statusFinal) {
        Pedido pedido = pedidoRascunho();
        pedido.adicionarPagamento(pagamentoConfirmado("110.00"));
        pedido.transitarPara(StatusProducaoEnum.AGUARDANDO_SINAL);
        if (statusFinal == StatusProducaoEnum.CANCELADO) {
            pedido.transitarPara(StatusProducaoEnum.CANCELADO);
        } else {
            pedido.transitarPara(StatusProducaoEnum.CONFIRMADO);
            pedido.transitarPara(StatusProducaoEnum.EM_PRODUCAO);
            if (statusFinal == StatusProducaoEnum.PRONTO_PARA_ENTREGA
                    || statusFinal == StatusProducaoEnum.ENTREGUE) {
                pedido.transitarPara(StatusProducaoEnum.PRONTO_PARA_ENTREGA);
            }
            if (statusFinal == StatusProducaoEnum.ENTREGUE) {
                pedido.transitarPara(StatusProducaoEnum.ENTREGUE);
            }
        }

        assertThrows(RegraDeNegocioException.class,
                () -> pedido.adicionarItem(item(new BigDecimal("10.00"), 1)));
    }

    @Test
    void naoDeveAtualizarDadosBasicosQuandoFinalizado() {
        Pedido pedido = pedidoRascunho();
        pedido.adicionarPagamento(pagamentoConfirmado("110.00"));
        pedido.transitarPara(StatusProducaoEnum.AGUARDANDO_SINAL);
        pedido.transitarPara(StatusProducaoEnum.CONFIRMADO);
        pedido.transitarPara(StatusProducaoEnum.EM_PRODUCAO);
        pedido.transitarPara(StatusProducaoEnum.PRONTO_PARA_ENTREGA);
        pedido.transitarPara(StatusProducaoEnum.ENTREGUE);

        assertThrows(RegraDeNegocioException.class, () -> pedido.atualizarDadosBasicos(
                LocalDateTime.now().plusDays(1), BigDecimal.ZERO, "obs", null));
    }

    @Test
    void deveExcluirLogicamenteTransitantoParaCancelado() {
        Pedido pedido = pedidoRascunho();

        pedido.excluirLogicamente();

        assertTrue(pedido.isDeletado());
        assertEquals(StatusProducaoEnum.CANCELADO, pedido.getStatusProducao());
    }

    @Test
    void naoDeveExcluirPedidoEntregue() {
        Pedido pedido = pedidoRascunho();
        pedido.adicionarPagamento(pagamentoConfirmado("110.00"));
        pedido.transitarPara(StatusProducaoEnum.AGUARDANDO_SINAL);
        pedido.transitarPara(StatusProducaoEnum.CONFIRMADO);
        pedido.transitarPara(StatusProducaoEnum.EM_PRODUCAO);
        pedido.transitarPara(StatusProducaoEnum.PRONTO_PARA_ENTREGA);
        pedido.transitarPara(StatusProducaoEnum.ENTREGUE);

        assertThrows(RegraDeNegocioException.class, pedido::excluirLogicamente);
    }

    @Test
    void naoDeveExcluirPedidoJaDeletado() {
        Pedido pedido = pedidoRascunho();
        pedido.excluirLogicamente();

        assertThrows(RegraDeNegocioException.class, pedido::excluirLogicamente);
    }

    @Test
    void deveExporItensEPagamentosComoListasImutaveis() {
        Pedido pedido = pedidoRascunho();

        assertThrows(UnsupportedOperationException.class,
                () -> pedido.getItens().add(item(new BigDecimal("1.00"), 1)));
        assertThrows(UnsupportedOperationException.class,
                () -> pedido.getPagamentos().add(pagamentoConfirmado("1.00")));
    }
}
