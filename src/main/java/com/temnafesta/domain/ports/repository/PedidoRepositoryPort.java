package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.application.dto.relatorio.PedidosPeriodoOutput;
import com.temnafesta.application.dto.relatorio.PedidosPorSemanaOutput;
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

    Optional<Pedido> buscarPorId(Long id);


    // O adaptador desta porta no JPA deverá buscar pedidos do cliente cujo
    // status_producao_id represente algo em andamento (diferente de ENTREGUE ou CANCELADO).
    boolean existePedidoEmAndamentoPorCliente(Long clienteId);

    Pedido atualizar(Pedido pedido);
    long contarPorStatus(StatusProducaoEnum status);
    List<Pedido> listarProximasRetiradas(LocalDateTime limite);
    List<Pedido> listarPedidos(String busca, StatusProducaoEnum status, Long eventoId);
    Optional<ItemPedido> buscarItemPorId(Long pedidoId, Long itemId);
    List<Pagamento> listarPagamentos(Long pedidoId);
    List<Pedido> listarPorFiltros(StatusProducaoEnum status, LocalDateTime inicio, LocalDateTime fim);

    Long countByDataPedidoBetweenAndDeletadoFalse(LocalDateTime localDateTime, LocalDateTime localDateTime1);

    Long countByStatusEPeriodo(StatusProducaoEnum statusEntregueProducao, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    BigDecimal somarFaturamentoNoPeriodo(LocalDateTime localDateTime, LocalDateTime localDateTime1);

    List<PedidosPorSemanaOutput> buscarPedidosAgrupadosPorSemana(LocalDateTime de, LocalDateTime ate);

    Page<PedidosPeriodoOutput> buscarPedidosPeriodoPaginado(LocalDateTime de, LocalDateTime ate, Pageable pageable);

    List<PedidosPeriodoOutput> buscarRelatorioDinamico(Integer eventoId, LocalDateTime dataInicio, LocalDateTime dataFim);
}