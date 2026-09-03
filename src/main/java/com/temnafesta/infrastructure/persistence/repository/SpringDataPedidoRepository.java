package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.infrastructure.persistence.entity.PedidoJpaEntity;
import com.temnafesta.infrastructure.projection.PedidosPeriodoProjection;
import com.temnafesta.infrastructure.projection.PedidosPorSemanaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataPedidoRepository extends JpaRepository<PedidoJpaEntity, Long> {

    @Query("SELECT p FROM PedidoJpaEntity p " +
            "WHERE (:status IS NULL OR p.statusProducao = :status) " +
            "AND (:inicio IS NULL OR p.dataEntrega >= :inicio) " +
            "AND (:fim IS NULL OR p.dataEntrega <= :fim) " +
            "AND p.deletado = false")
    List<PedidoJpaEntity> listarPorFiltros(@Param("status") StatusProducaoEnum status,
                                           @Param("inicio") LocalDateTime inicio,
                                           @Param("fim") LocalDateTime fim);

    Long countByDataPedidoBetween(LocalDateTime de, LocalDateTime ate);

    // Pedidos por status status entrege e periodo
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.statusProducao.id = :statusId AND p.dataPedido BETWEEN :de AND :ate")
    Long countByStatusEPeriodo(
            @Param("statusId") Integer statusId,
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate
    );

    // Faturamento por periodo
    @Query(value = "SELECT COALESCE(SUM(pag.valor), 0) " +
            "FROM pagamento pag " +
            "WHERE pag.data_pagamento BETWEEN :de AND :ate",
            nativeQuery = true)
    BigDecimal somarFaturamentoNoPeriodo(
            @Param("statusId") Integer statusId,
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate);


    // Retorna a quantidade de pedidos agrupado por rotulos como "Sem 19" para o grafico de pedidos por semana do relatorio consumir
    @Query(value =
            "SELECT " +
                    "   CONCAT('Sem ', ROW_NUMBER() OVER (ORDER BY t.semana)) AS rotulo, " +
                    "   CONCAT( " +
                    "       DATE_FORMAT(t.data_inicio, '%d/%m'), " +
                    "       ' - ', " +
                    "       DATE_FORMAT(t.data_fim, '%d/%m') " +
                    "   ) AS periodo, " +
                    "   t.quantidade " +
                    "FROM ( " +
                    "   SELECT " +
                    "       WEEK(p.data_pedido) AS semana, " +
                    "       MIN(p.data_pedido) AS data_inicio, " +
                    "       MAX(p.data_pedido) AS data_fim, " +
                    "       COUNT(p.id) AS quantidade " +
                    "   FROM pedido p " +
                    "   WHERE p.data_pedido BETWEEN :de AND :ate " +
                    "   GROUP BY WEEK(p.data_pedido) " +
                    ") t " +
                    "ORDER BY t.semana ASC",
            nativeQuery = true)
    List<PedidosPorSemanaProjection> buscarPedidosAgrupadosPorSemana(
            @Param("de") LocalDateTime de,
            @Param("ate") LocalDateTime ate
    );

    // Retorna os pedidos paginados por periodo
    @Query(value = "SELECT " +
            "  p.id AS id, " +
            "  p.data_pedido AS dataPedido, " +
            "  c.nome AS clienteNome, " +
            "  e.nome AS eventoNome, " +
            "  p.valor_total AS valorTotal, " +
            "  COALESCE(SUM(pag.valor), 0) AS valorPago, " +
            "  s.nome AS statusNome " +
            "FROM pedido p " +
            "INNER JOIN cliente c ON p.cliente_id = c.id " +
            "INNER JOIN evento e ON p.evento_id = e.id " +
            "INNER JOIN status_producao s ON p.status_producao_id = s.id " +
            "LEFT JOIN pagamento pag ON pag.pedido_id = p.id " +
            "WHERE p.data_pedido BETWEEN :de AND :ate " +
            "GROUP BY p.id, p.data_pedido, c.nome, e.nome, p.valor_total, s.nome",
            countQuery = "SELECT COUNT(*) FROM pedido p WHERE p.data_pedido BETWEEN :de AND :ate",
            nativeQuery = true)
    Page<PedidosPeriodoProjection> buscarPedidosPeriodoPaginado(
            @Param("de") java.time.LocalDateTime de,
            @Param("ate") java.time.LocalDateTime ate,
            Pageable pageable
    );

    @Query("""
    SELECT p.id AS id,
           p.dataPedido AS dataPedido,
           c.nome AS clienteNome,
           e.nome AS eventoNome,
           p.valorTotal AS valorTotal,
           COALESCE((SELECT SUM(pag.valor) FROM Pagamento pag WHERE pag.pedido.id = p.id), 0) AS valorPago,
           s.nome AS statusNome
    FROM Pedido p
    JOIN p.cliente c
    JOIN p.evento e
    JOIN p.statusProducao s
    WHERE (:eventoId IS NULL OR e.id = :eventoId)
      AND (:dataInicio IS NULL OR p.dataPedido >= :dataInicio)
      AND (:dataFim IS NULL OR p.dataPedido <= :dataFim)
    ORDER BY p.dataPedido DESC
""")
    List<PedidosPeriodoProjection> buscarRelatorioDinamico(
            @Param("eventoId") Integer eventoId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );
}