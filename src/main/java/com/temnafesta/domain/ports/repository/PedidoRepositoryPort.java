package com.temnafesta.domain.ports.repository;

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
    List<Pedido> listarPorFiltros(StatusProducaoEnum status, LocalDateTime inicio, LocalDateTime fim);

    Long countByDataPedidoBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);

    Long countByStatusEPeriodo(Integer statusEntregueProducao, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    BigDecimal somarFaturamentoNoPeriodo(Integer statusEntregueProducao, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    List<PedidosPorSemanaOutput> buscarPedidosAgrupadosPorSemana(LocalDateTime de, LocalDateTime ate);

    Page<PedidosPeriodoOutput> buscarPedidosPeriodoPaginado(LocalDateTime de, LocalDateTime ate, Pageable pageable);

    List<PedidosPeriodoOutput> buscarRelatorioDinamico(Integer eventoId, LocalDateTime dataInicio, LocalDateTime dataFim);
}