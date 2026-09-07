package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.vo.StatusPagamentoEnum;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.domain.vo.TipoPagamentoEnum;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste de contrato do {@link PedidoRepositoryPort} usando um fake in-memory.
 * Documenta a semântica esperada pelos use cases (espelha o
 * PedidoRepositoryAdapter): upsert em salvar/atualizar, Optional vazio quando
 * ausente, lista vazia de pagamentos para pedido inexistente e "em andamento"
 * = status diferente de ENTREGUE/CANCELADO. Não toca em banco de dados.
 */
class PedidoRepositoryPortContractTest {

    static class PedidoRepositoryPortFake implements PedidoRepositoryPort {
        private final Map<Long, Pedido> banco = new HashMap<>();
        private final AtomicLong sequencia = new AtomicLong(1);

        @Override
        public Pedido salvar(Pedido pedido) {
            return guardar(pedido);
        }

        @Override
        public Pedido atualizar(Pedido pedido) {
            return guardar(pedido);
        }

        private Pedido guardar(Pedido pedido) {
            if (pedido.getId() == null) {
                Pedido comId = copiarComId(pedido, sequencia.getAndIncrement());
                banco.put(comId.getId(), comId);
                return comId;
            }
            banco.put(pedido.getId(), pedido);
            return pedido;
        }

        private Pedido copiarComId(Pedido pedido, Long id) {
            return new Pedido(id, pedido.getDataPedido(), pedido.getDataEntrega(),
                    pedido.getTaxaEntrega(), pedido.getObservacao(), pedido.getStatusProducao(),
                    pedido.getClienteId(), pedido.getUsuarioId(), pedido.getEventoId(),
                    pedido.getEnderecoEntregaId(), pedido.getItens(), pedido.getPagamentos());
        }

        @Override
        public Optional<Pedido> buscarPorId(Long id) {
            return Optional.ofNullable(banco.get(id));
        }

        @Override
        public List<Pedido> listarPorFiltros(StatusProducaoEnum status, LocalDateTime inicio, LocalDateTime fim) {
            return banco.values().stream()
                    .filter(p -> status == null || p.getStatusProducao() == status)
                    .filter(p -> inicio == null || !p.getDataPedido().isBefore(inicio))
                    .filter(p -> fim == null || !p.getDataPedido().isAfter(fim))
                    .toList();
        }

        @Override
        public boolean existePedidoEmAndamentoPorCliente(Long clienteId) {
            return banco.values().stream()
                    .filter(p -> clienteId.equals(p.getClienteId()))
                    .map(Pedido::getStatusProducao)
                    .anyMatch(s -> !EnumSet.of(StatusProducaoEnum.ENTREGUE, StatusProducaoEnum.CANCELADO).contains(s));
        }

        @Override
        public long contarPorStatus(StatusProducaoEnum status) {
            return banco.values().stream().filter(p -> p.getStatusProducao() == status).count();
        }

        @Override
        public List<Pedido> listarProximasRetiradas(LocalDateTime limite) {
            return banco.values().stream()
                    .filter(p -> p.getDataEntrega() != null && !p.getDataEntrega().isAfter(limite))
                    .toList();
        }

        @Override
        public List<Pedido> listarPedidos(String busca, StatusProducaoEnum status, Long eventoId) {
            return banco.values().stream()
                    .filter(p -> status == null || p.getStatusProducao() == status)
                    .filter(p -> eventoId == null || eventoId.equals(p.getEventoId()))
                    .toList();
        }

        @Override
        public Optional<ItemPedido> buscarItemPorId(Long pedidoId, Long itemId) {
            return buscarPorId(pedidoId)
                    .flatMap(p -> p.getItens().stream().filter(i -> itemId.equals(i.getId())).findFirst());
        }

        @Override
        public List<Pagamento> listarPagamentos(Long pedidoId) {
            return buscarPorId(pedidoId).map(p -> new ArrayList<>(p.getPagamentos())).orElseGet(ArrayList::new);
        }
    }

    private static Pedido pedidoNovo(Long clienteId) {
        return new Pedido(null, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                BigDecimal.ZERO, null, StatusProducaoEnum.RASCUNHO,
                clienteId, 1L, null, null,
                List.of(new ItemPedido(10L, 1L, 1, new BigDecimal("20.00"), null)), null);
    }

    private final PedidoRepositoryPort port = new PedidoRepositoryPortFake();

    @Test
    void deveAtribuirIdAoSalvarPedidoNovoERetornarEmBusca() {
        Pedido salvo = port.salvar(pedidoNovo(1L));

        assertTrue(salvo.getId() != null);
        assertEquals(salvo.getId(), port.buscarPorId(salvo.getId()).orElseThrow().getId());
    }

    @Test
    void deveRetornarVazioAoBuscarIdInexistente() {
        assertTrue(port.buscarPorId(999L).isEmpty());
    }

    @Test
    void deveDetectarPedidoEmAndamentoIgnorandoEntregueECancelado() {
        Pedido emAndamento = port.salvar(pedidoNovo(7L));
        assertTrue(port.existePedidoEmAndamentoPorCliente(7L));

        Pedido entregue = new Pedido(emAndamento.getId(), emAndamento.getDataPedido(),
                emAndamento.getDataEntrega(), BigDecimal.ZERO, null, StatusProducaoEnum.ENTREGUE,
                8L, 1L, null, null, List.of(), null);
        port.salvar(entregue);
        assertFalse(port.existePedidoEmAndamentoPorCliente(8L));
        Pedido cancelado = new Pedido(77L, emAndamento.getDataPedido(),
                emAndamento.getDataEntrega(), BigDecimal.ZERO, null, StatusProducaoEnum.CANCELADO,
                9L, 1L, null, null, List.of(), null);
        port.salvar(cancelado);
        assertFalse(port.existePedidoEmAndamentoPorCliente(9L));
        assertFalse(port.existePedidoEmAndamentoPorCliente(999L));
    }

    @Test
    void deveContarPorStatus() {
        port.salvar(pedidoNovo(1L));
        port.salvar(pedidoNovo(2L));

        assertEquals(2, port.contarPorStatus(StatusProducaoEnum.RASCUNHO));
        assertEquals(0, port.contarPorStatus(StatusProducaoEnum.ENTREGUE));
    }

    @Test
    void deveBuscarItemPorId() {
        Pedido salvo = port.salvar(pedidoNovo(1L));

        assertTrue(port.buscarItemPorId(salvo.getId(), 10L).isPresent());
        assertTrue(port.buscarItemPorId(salvo.getId(), 999L).isEmpty());
        assertTrue(port.buscarItemPorId(999L, 10L).isEmpty());
    }

    @Test
    void deveListarPagamentosVazioParaPedidoInexistente() {
        assertTrue(port.listarPagamentos(999L).isEmpty());
    }

    @Test
    void deveListarPagamentosDoPedido() {
        Pedido base = pedidoNovo(1L);
        base.adicionarPagamento(new Pagamento(null, new BigDecimal("10.00"), LocalDateTime.now(),
                TipoPagamentoEnum.SINAL, StatusPagamentoEnum.CONFIRMADO, 1L, 1L));
        Pedido salvo = port.salvar(base);

        assertEquals(1, port.listarPagamentos(salvo.getId()).size());
    }
}
