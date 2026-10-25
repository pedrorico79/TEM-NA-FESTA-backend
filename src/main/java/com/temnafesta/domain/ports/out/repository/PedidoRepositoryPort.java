package com.temnafesta.domain.ports.out.repository;

import com.temnafesta.application.dto.relatorio.PedidosPeriodoOutput;
import com.temnafesta.application.dto.relatorio.PedidosPorSemanaOutput;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepositoryPort {
    Pedido salvar(Pedido pedido);
    Pedido atualizarStatus(Long pedidoId, StatusProducaoEnum novoStatus);

    Optional<Pedido> buscarPorId(Long id);
    Optional<Pedido> buscarPorIdParaRegistrarPagamento(Long id);
    Optional<Pedido> buscarPorChaveIdempotencia(String chave);

    // O adaptador desta porta no JPA deverá buscar pedidos do cliente cujo
    // status_producao represente algo em andamento (diferente de ENTREGUE ou CANCELADO).
    boolean existePedidoEmAndamentoPorCliente(Long clienteId);

    Pedido atualizar(Pedido pedido);
    long contarPorStatus(StatusProducaoEnum status);
    long contarPedidosComPagamentoPendente();
    List<Pedido> listarProximasRetiradas(LocalDateTime limite);
    List<Pedido> listarPedidos(String busca, StatusProducaoEnum status, Long eventoId);
    Page<Pedido> listarPedidos(String busca, StatusProducaoEnum status, Long eventoId, Pageable pageable);
    default Page<Pedido> listarPedidos(String busca, StatusProducaoEnum status, Long eventoId,
                                       boolean pagamentoPendente, Pageable pageable) {
        if (pagamentoPendente) {
            throw new UnsupportedOperationException("Este repositório não implementa o filtro de pagamentos pendentes.");
        }
        return listarPedidos(busca, status, eventoId, pageable);
    }
    default Page<Pedido> listarPedidos(String busca, StatusProducaoEnum status, Long eventoId,
                                       boolean pagamentoPendente, boolean ativos, Pageable pageable) {
        if (pagamentoPendente || ativos) {
            throw new UnsupportedOperationException("Este repositório não implementa os filtros especiais de pedidos.");
        }
        return listarPedidos(busca, status, eventoId, pageable);
    }
    Optional<ItemPedido> buscarItemPorId(Long pedidoId, Long itemId);
    List<Pagamento> listarPagamentos(Long pedidoId);
    List<Pedido> listarPorFiltros(StatusProducaoEnum status, LocalDateTime inicio, LocalDateTime fim);

    Long countByDataPedidoBetweenAndDeletadoFalse(LocalDateTime inicio, LocalDateTime fim, Long eventoId);
    Long countByStatusEPeriodo(StatusProducaoEnum status, LocalDateTime inicio, LocalDateTime fim, Long eventoId);
    BigDecimal somarFaturamentoNoPeriodo(LocalDateTime inicio, LocalDateTime fim, Long eventoId);
    List<PedidosPorSemanaOutput> buscarPedidosAgrupadosPorSemana(LocalDateTime de, LocalDateTime ate, Long eventoId);
    Page<PedidosPeriodoOutput> buscarPedidosPeriodoPaginado(LocalDateTime de, LocalDateTime ate, Pageable pageable);
    List<PedidosPeriodoOutput> buscarRelatorioDinamico(Integer eventoId, LocalDateTime dataInicio, LocalDateTime dataFim);
}
